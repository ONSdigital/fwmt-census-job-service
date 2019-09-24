package uk.gov.ons.census.fwmt.jobservice.converter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingCached;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CcsCaseExtension;
import uk.gov.ons.census.fwmt.common.data.modelcase.Contact;
import uk.gov.ons.census.fwmt.common.data.modelcase.Location;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;
import uk.gov.ons.census.fwmt.jobservice.entity.CCSOutcomeStore;
import uk.gov.ons.census.fwmt.jobservice.message.MessageConverter;

import static uk.gov.ons.census.fwmt.jobservice.utils.JobServiceUtils.setAddress;

@Component("CCS")
public class CCSIVConverter implements CometConverter {

  @Autowired
  private CCSOutcomeStore ccsOutcomeStore;

  @Autowired
  private MessageConverter messageConverter;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public CaseRequest convert(CreateFieldWorkerJobRequest ingest) throws GatewayException {

    CcsCaseExtension ccsCaseExtension = new CcsCaseExtension();
    CCSPropertyListingCached ccsPropertyListingCached;
    CaseRequest caseRequest = new CaseRequest();
    Location location = new Location();

    caseRequest.setReference(ingest.getCaseReference());
    caseRequest.setType(CaseRequest.TypeEnum.CCSIV);
    caseRequest.setSurveyType(ingest.getSurveyType());
    caseRequest.setEstabType(ingest.getEstablishmentType());
    // unsure of this one
    caseRequest.setRequiredOfficer(ingest.getMandatoryResource());
    caseRequest.setCoordCode(ingest.getCoordinatorId());
    caseRequest.setContact(setContact(ingest));
    // check this method

    location.setLat(ingest.getAddress().getLatitude().floatValue());
    location.set_long(ingest.getAddress().getLongitude().floatValue());
    caseRequest.setLocation(location);
    // Removed mapping from actionRequest for the ccsQuestionnaireURL;
    // this will be derived from an environment variable and the caseId in the CCS specific mapping
    ccsCaseExtension.setQuestionnaireUrl(ingest.getCcsQuestionnaireURL());
    caseRequest.setCcs(ccsCaseExtension);

    ccsPropertyListingCached = getCachedOutcomeDetails(ingest);

    ingest.setAddress(updateAddressWithOa(ingest, ccsPropertyListingCached.getOa()));

    caseRequest.setSpecialInstructions(getSpecialInstructions(ccsPropertyListingCached));
    caseRequest.setAddress(setAddress(ingest));
    return caseRequest;
  }

  private Contact setContact(CreateFieldWorkerJobRequest ingest) {
    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
    return contact;
  }

  private CCSPropertyListingCached getCachedOutcomeDetails(CreateFieldWorkerJobRequest ingest) throws GatewayException {
    String retrievedCache = ccsOutcomeStore.retrieveCache(String.valueOf(ingest.getCaseId()));

    CCSPropertyListingCached ccsPropertyListingCached = messageConverter.convertMessageToDTO(CCSPropertyListingCached.class, retrievedCache);

    return ccsPropertyListingCached;
  }

  private String getSpecialInstructions(CCSPropertyListingCached cachedSpecialInstructions) throws GatewayException {
    try {
      String ccsPLToTM = objectMapper.writeValueAsString(cachedSpecialInstructions);
      return ccsPLToTM;
    } catch (JsonProcessingException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to convert CCSPL cached data to string");
    }
  }

  private Address updateAddressWithOa(CreateFieldWorkerJobRequest ingest, String cachedOa) {
    Address updatedAddress = new Address();

    updatedAddress = ingest.getAddress();
    updatedAddress.setOa(cachedOa);

    return updatedAddress;
  }

  @Override
  public CasePauseRequest convertCancel(CancelFieldWorkerJobRequest cancelIngest) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CaseRequest convertUpdate(UpdateFieldWorkerJobRequest ingest, ModelCase modelCase) {
    throw new UnsupportedOperationException();
  }
}

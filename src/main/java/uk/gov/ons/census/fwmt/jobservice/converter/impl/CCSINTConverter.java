package uk.gov.ons.census.fwmt.jobservice.converter.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingOutcome;
import uk.gov.ons.census.fwmt.common.data.ccs.CareCode;
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

import java.util.ArrayList;
import java.util.List;

import static uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest.TypeEnum.CCS;
import static uk.gov.ons.census.fwmt.jobservice.utils.JobServiceUtils.setAddress;

@Component("CCS")
public class CCSINTConverter implements CometConverter {

  @Autowired
  private CCSOutcomeStore ccsOutcomeStore;

  @Autowired
  private MessageConverter messageConverter;

  @Override
  public CaseRequest convert(CreateFieldWorkerJobRequest ingest) throws GatewayException {

    CcsCaseExtension ccsCaseExtension = new CcsCaseExtension();
    CCSPropertyListingOutcome ccsPropertyListingCached = getCachedOutcomeDetails(ingest);
    CaseRequest caseRequest = new CaseRequest();
    Location location = new Location();

    caseRequest.setReference(ingest.getCaseReference());
    caseRequest.setType(CCS);
    caseRequest.setSurveyType(ingest.getSurveyType());
    caseRequest.setCategory(ingest.getCategory());
    caseRequest.setRequiredOfficer(ccsPropertyListingCached.getUsername());
    caseRequest.setCoordCode(ingest.getCoordinatorId());
    caseRequest.setContact(setContact(ingest));

    if (ingest.getEstablishmentType().equals("Household")) {
      caseRequest.setEstabType("HH");
      ccsCaseExtension.setQuestionnaireUrl(ingest.getCcsQuestionnaireURL());
    } else if (ingest.getEstablishmentType().equals("CE")) {
      caseRequest.setEstabType("CE");
    }

    location.setLat(ingest.getAddress().getLatitude().floatValue());
    location.set_long(ingest.getAddress().getLongitude().floatValue());
    caseRequest.setLocation(location);
    caseRequest.setCcs(ccsCaseExtension);

    ingest.setAddress(updateAddressWithOa(ingest, ccsPropertyListingCached.getAddress().getOa()));

    caseRequest.setSpecialInstructions(getAdditionalInformation(ccsPropertyListingCached, true));
    caseRequest.setDescription(getAdditionalInformation(ccsPropertyListingCached, false));
    caseRequest.setAddress(setAddress(ingest));
    return caseRequest;
  }

  private Contact setContact(CreateFieldWorkerJobRequest ingest) {
    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
    return contact;
  }

  private CCSPropertyListingOutcome getCachedOutcomeDetails(CreateFieldWorkerJobRequest ingest)
      throws GatewayException {
    String retrievedCache = ccsOutcomeStore.retrieveCache(String.valueOf(ingest.getCaseId()));

    return messageConverter.convertMessageToDTO(CCSPropertyListingOutcome.class, retrievedCache);
  }

  private String getAdditionalInformation(CCSPropertyListingOutcome cachedSpecialInstructions, boolean isSpecialInstruction) {
    List<String> additionalInformation = new ArrayList<>();
    String accessInfo;
    String careCode;
    String additionalInformationToString;

    if (isSpecialInstruction) {
      if (cachedSpecialInstructions.getAccessInfo() != null) {
        accessInfo = cachedSpecialInstructions.getAccessInfo();
      } else {
        accessInfo = "None";
      }
      additionalInformation.add("Access Info: " + accessInfo);
      if (!StringUtils.isEmpty(cachedSpecialInstructions.getCareCodes())) {
        careCode = formatCareCodeList(cachedSpecialInstructions.getCareCodes());
        additionalInformation.add("CareCodes: " + careCode);
      } else {
        additionalInformation.add("CareCode: None");
      }
    } else {
      additionalInformation.add("Primary Outcome: " + cachedSpecialInstructions.getPrimaryOutcome());
      additionalInformation.add("Secondary Outcome: " + cachedSpecialInstructions.getSecondaryOutcome());
    }

    additionalInformationToString = additionalInformation.toString();
    additionalInformationToString = additionalInformationToString.replaceAll("[\\[\\](){}]", "");
    additionalInformationToString = additionalInformationToString.replaceAll("," , "\n");

    return additionalInformationToString;
  }

  private Address updateAddressWithOa(CreateFieldWorkerJobRequest ingest, String cachedOa) {
    Address updatedAddress = ingest.getAddress();
    updatedAddress.setOa(cachedOa);

    return updatedAddress;
  }

  private String formatCareCodeList (List<CareCode> careCode) {
    String careCodes = "";

    for (int i =0; i <= careCode.size(); i++) {
      careCodes = careCode.toString();
      careCodes = careCodes.replaceAll("careCode=", "");
      careCodes = careCodes.replaceAll("CareCode", "");
      careCodes = careCodes.replaceAll("[\\[\\](){}]","");
    }
    return careCodes;
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
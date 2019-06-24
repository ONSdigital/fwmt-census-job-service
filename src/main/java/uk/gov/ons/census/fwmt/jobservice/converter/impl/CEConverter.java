package uk.gov.ons.census.fwmt.jobservice.converter.impl;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CeCaseExtension;
import uk.gov.ons.census.fwmt.common.data.modelcase.Contact;
import uk.gov.ons.census.fwmt.common.data.modelcase.Geography;
import uk.gov.ons.census.fwmt.common.data.modelcase.Location;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;

import static uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest.TypeEnum.CE;
import static uk.gov.ons.census.fwmt.jobservice.utils.JobServiceUtils.addAddressLines;

@Component("CE")
public class CEConverter implements CometConverter {

  @Override public CaseRequest convert(CreateFieldWorkerJobRequest ingest) throws GatewayException {
    Address address = new Address();
    CaseRequest caseRequest = new CaseRequest();
    CeCaseExtension ceCaseExtension = new CeCaseExtension();
    Geography geography = new Geography();
    Location location = new Location();

    caseRequest.setReference(ingest.getCaseReference());
    caseRequest.setType(CE);
    caseRequest.setSurveyType(ingest.getCaseType());
    // Category is not yet in the feed
    caseRequest.setCategory("CE");
    //    caseRequest.setCategory(ingest.getCategory());
    caseRequest.setEstabType(ingest.getEstablishmentType());
    caseRequest.setRequiredOfficer(ingest.getMandatoryResource());
    caseRequest.setCoordCode(ingest.getCoordinatorId());

    Contact contact = new Contact();
    contact.setOrganisationName(ingest.getContact().getOrganisationName());
    caseRequest.setContact(contact);


    // arin not yet part of Comet
    //    try {
    //      address.setArid(Long.valueOf(ingest.getAddress().getArid()));
    //    } catch (Exception e) {
    //      // if a problem resolving ARID, null is fine
    //    }
    try {
      address.setUprn(Long.valueOf(ingest.getAddress().getUprn()));
    } catch (Exception e) {
      // TODO is this still the case?
      // if a problem resolving UPRN, null is fine
    }

    address.setLines(addAddressLines(ingest));
    address.setTown(ingest.getAddress().getTownName());
    address.setPostcode(ingest.getAddress().getPostCode());


    geography.setOa(ingest.getAddress().getOa());
    address.setGeography(geography);

    caseRequest.setAddress(address);

    location.setLat(ingest.getAddress().getLatitude().floatValue());
    location.set_long(ingest.getAddress().getLongitude().floatValue());
    caseRequest.setLocation(location);

    /*
        These are still needed as part of a create house hold,
        unsure where they are derived from.
    */
    caseRequest.setDescription("CENSUS");
    caseRequest.setSpecialInstructions("Special Instructions");
    //    caseRequest.setDescription(ingest.getDescription()) ;
    //    caseRequest.setSpecialInstructions(ingest.getSpecialInstructions());

    caseRequest.setUaa(ingest.isUua());
    caseRequest.setSai(ingest.isSai());
    caseRequest.setBlankFormReturned(ingest.isBlankFormReturned());

    ceCaseExtension.setExpectedResponses(ingest.getCeExpectedResponses());
    caseRequest.setCe(ceCaseExtension);

    return caseRequest;
  }

  @Override public CaseRequest convertUpdate(UpdateFieldWorkerJobRequest ingest, ModelCase modelCase)
      throws GatewayException {
    throw new UnsupportedOperationException();
  }

  @Override public CasePauseRequest convertCancel(CancelFieldWorkerJobRequest cancelIngest) throws GatewayException {
    throw new UnsupportedOperationException();
  }
}

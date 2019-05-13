package uk.gov.ons.census.fwmt.jobservice.converter.impl;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Contact;
import uk.gov.ons.census.fwmt.common.data.modelcase.Geography;
import uk.gov.ons.census.fwmt.common.data.modelcase.Location;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;

import static uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest.TypeEnum.HH;
import static uk.gov.ons.census.fwmt.jobservice.utils.JobServiceUtils.addAddressLines;

@Component("Household")
public class HouseholdConverter implements CometConverter {

  @Override
  public CaseRequest convert(CreateFieldWorkerJobRequest ingest) {
    CaseRequest caseRequest = new CaseRequest();
    caseRequest.setReference(ingest.getCaseReference());
    caseRequest.setType(HH);
    caseRequest.setSurveyType(ingest.getCaseType());
    // Category is not yet in the feed
    caseRequest.setCategory("Household");
//    caseRequest.setCategory(ingest.getCategory());
    caseRequest.setEstabType(ingest.getEstablishmentType());
    caseRequest.setCoordCode(ingest.getCoordinatorId());

    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
    contact.setOrganisationName(ingest.getContact().getOrganisationName());
    contact.setPhone(ingest.getContact().getPhoneNumber());
    contact.setEmail(ingest.getContact().getEmailAddress());
    caseRequest.setContact(contact);

    Address address = new Address();
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

    Geography geography = new Geography();
    geography.setOa(ingest.getAddress().getOa());
    address.setGeography(geography);

    caseRequest.setAddress(address);

    Location location = new Location();
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

    return caseRequest;
  }

  @Override
  public CasePauseRequest convertPause(CancelFieldWorkerJobRequest ingest) {
    CasePauseRequest pauseRequest = new CasePauseRequest();
    pauseRequest.setId(String.valueOf(ingest.getCaseId()));
    pauseRequest.setUntil(ingest.getUntil());
    pauseRequest.setReason(ingest.getReason());

    return pauseRequest;
  }
}

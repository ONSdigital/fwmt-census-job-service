package uk.gov.ons.census.fwmt.jobservice.converter.impl;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePause;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Contact;
import uk.gov.ons.census.fwmt.common.data.modelcase.Geography;
import uk.gov.ons.census.fwmt.common.data.modelcase.Location;
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
    caseRequest.setSurveyType(ingest.getSurveyType());
    caseRequest.setCategory(ingest.getCategory());
    caseRequest.setEstabType(ingest.getEstablishmentType());
    caseRequest.setCoordCode("EX23");
    caseRequest.setDescription("CENSUS");
    caseRequest.setSpecialInstructions("Special Instructions");
    // TODO hard coded above
    //    caseRequest.setCoordCode(ingest.getCoordinatorId());
    //    caseRequest.setDescription(ingest.getDescription()) ;
    //    caseRequest.setSpecialInstructions(ingest.getSpecialInstructions());
    caseRequest.setUaa(false);
    caseRequest.setSai(ingest.isSai());
    caseRequest.setUaa(ingest.isUua());

    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
    caseRequest.setContact(contact);

    Address address = new Address();
    // arin not yet part of Comet
    try {
      address.setUprn(Long.valueOf(ingest.getAddress().getUprn()));
    } catch (Exception e) {
      // TODO is this still the case?
      // if a problem resolving UPRN, null is fine
    }

    Geography geography = new Geography();
    geography.setOa(ingest.getAddress().getOa());
    address.setGeography(geography);

    address.setLines(addAddressLines(ingest));
    address.setPostcode(ingest.getAddress().getPostCode());
    caseRequest.setAddress(address);

    Location location = new Location();
    location.set_long(ingest.getAddress().getLatitude().floatValue());
    location.setLat(ingest.getAddress().getLongitude().floatValue());
    caseRequest.setLocation(location);

    // TODO missing fields in CasePause
    CasePause casePause = new CasePause();
    casePause.setUntil(ingest.getPause().getHoldUntil());
    casePause.setReason(ingest.getPause().getReason());

    return caseRequest;
  }
}

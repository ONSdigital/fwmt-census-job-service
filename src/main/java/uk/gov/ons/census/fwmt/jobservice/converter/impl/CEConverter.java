package uk.gov.ons.census.fwmt.jobservice.converter.impl;

import static uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase.StateEnum.OPEN;
import static uk.gov.ons.census.fwmt.jobservice.utils.JobServiceUtils.addAddressLines;

import java.time.Instant;

import org.springframework.stereotype.Component;

import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.modelcase.Contact;
import uk.gov.ons.census.fwmt.common.data.modelcase.LatLong;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;

@Component("CE")
public class CEConverter implements CometConverter {

  private static final String CASE_TYPE_CE = "CE";

  @Override
  public ModelCase convert(CreateFieldWorkerJobRequest ingest) {
    ModelCase modelCase = new ModelCase();
    Instant instant = Instant.now();
    modelCase.setId(ingest.getCaseId().toString());
    modelCase.setReference(ingest.getJobIdentity());
    modelCase.setCaseType(CASE_TYPE_CE);
    modelCase.setState(OPEN);

    // TODO not yet implemented in Canonical
    //modelCase.setCategory(ingest.getAddress().getCategory());

    modelCase.setEstabType(ingest.getAdditionalProperties().get("establishmentType"));
    modelCase.setCoordCode("EX23");
    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
    contact.setOrganisationName(ingest.getAddress().getOrganisationName());
    contact.setPhone(ingest.getContact().getPhoneNumber());
    contact.setEmail(ingest.getContact().getEmail());
    modelCase.setContact(contact);

    Address address = new Address();
    try {
      address.setUprn(Long.parseLong(ingest.getAdditionalProperties().get("uprn")));
    }catch (Exception e) {
      // if a problem resolving uprn, null is fine.
    }
    address.setLines(addAddressLines(ingest));
    address.setPostCode(ingest.getAddress().getPostCode());
    modelCase.setAddress(address);

    LatLong latLong = new LatLong();
    latLong.setLat(ingest.getAddress().getLatitude().doubleValue());
    latLong.set_long(ingest.getAddress().getLongitude().doubleValue());
    modelCase.setLocation(latLong);

    modelCase.setHtc(0);
    modelCase.setPriority(0);
    modelCase.setDescription("CENSUS");
    modelCase.setHoldUntil(instant.toString());

    return modelCase;
  }

}

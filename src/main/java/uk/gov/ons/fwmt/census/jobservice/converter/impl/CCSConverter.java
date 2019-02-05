package uk.gov.ons.fwmt.census.jobservice.converter.impl;

import static uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase.StateEnum.OPEN;
import static uk.gov.ons.fwmt.census.jobservice.utils.JobServiceUtils.addAddressLines;

import java.time.Instant;

import org.springframework.stereotype.Component;

import uk.gov.ons.fwmt.census.jobservice.comet.dto.Address;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.Contact;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.LatLong;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;

@Component("CCS")
public class CCSConverter implements CometConverter {
  private static final String CASE_TYPE_CCS = "CCS";

  @Override
  public ModelCase convert(CreateFieldWorkerJobRequest ingest) {
    ModelCase modelCase = new ModelCase();
    Instant instant = Instant.now();
    modelCase.setId(ingest.getAdditionalProperties().get("caseId"));
    modelCase.setReference(ingest.getJobIdentity());
    modelCase.setCaseType(CASE_TYPE_CCS);
    modelCase.setState(OPEN);

    // TODO not yet implemented in Canonical
    //modelCase.setCategory(ingest.getAddress().getCategory());
    
    modelCase.setCoordCode("EX23");
    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
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

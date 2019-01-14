package uk.gov.ons.fwmt.census.jobservice.converter.impl;

import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.Address;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.Contact;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.LatLong;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

import java.time.Instant;

import static uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase.StateEnum.OPEN;
import static uk.gov.ons.fwmt.census.jobservice.utils.JobServiceUtils.addAddressLines;

@Component("HH")
public class HouseholdConverter implements CometConverter {

  private static final String CASE_TYPE_HH = "HH";

  @Override
  public ModelCase convert(FWMTCreateJobRequest ingest) {
    ModelCase modelCase = new ModelCase();
    Instant instant = Instant.now();
    modelCase.setId(ingest.getAdditionalProperties().get("caseId"));
    modelCase.setReference(ingest.getJobIdentity());
    modelCase.setCaseType(CASE_TYPE_HH); 
    modelCase.setState(OPEN);

    // TODO not yet implemented in Canonical
    //modelCase.setCategory(ingest.getAddress().getCategory());

    modelCase.setEstabType(ingest.getAdditionalProperties().get("establishmentType"));
    modelCase.setCoordCode("EX23");
    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
    contact.setPhone(ingest.getContact().getPhoneNumber());
    contact.setEmail(ingest.getContact().getEmail());
    modelCase.setContact(contact);

    Address address = new Address();
    Long uprn = null;
    try {
      uprn = Long.parseLong(ingest.getAdditionalProperties().get("uprn"));
    }catch (Exception e) {
      // if a problem resolving uprn, null is fine.
    }
    
    address.setUprn(uprn);
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

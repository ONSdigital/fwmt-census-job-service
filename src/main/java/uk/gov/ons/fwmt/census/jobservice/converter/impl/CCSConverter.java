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
import static uk.gov.ons.fwmt.census.jobservice.utils.JobServiceUtility.addAddressLines;

@Component("CCS")
public class CCSConverter implements CometConverter {
  private static final String CASE_TYPE_CCS = "CCS";

  @Override
  public ModelCase convert(FWMTCreateJobRequest ingest) {
    ModelCase modelCase = new ModelCase();
    Instant instant = Instant.now();
    modelCase.setId(ingest.getAdditionalProperties().get("caseId"));
    modelCase.setReference(ingest.getJobIdentity());
    modelCase.setCaseType(CASE_TYPE_CCS);
    modelCase.setState(OPEN);
    modelCase.setCategory("category");
    modelCase.setEstabType("Household");
    modelCase.setCoordCode("EX23");
    Contact contact = new Contact();
    contact.setName(ingest.getAddress().getPostCode());
    contact.setOrganisationName(ingest.getAddress().getOrganisationName());
    contact.setPhone(ingest.getContact().getPhoneNumber());
    contact.setEmail(ingest.getContact().getEmail());
    modelCase.setContact(contact);

    Address address = new Address();
    address.setUprn(0l);
    address.setLines(addAddressLines(ingest));
    address.setPostCode(ingest.getAddress().getPostCode());
    modelCase.setAddress(address);

    LatLong latLong = new LatLong();
    latLong.setLat(ingest.getAddress().getLatitude().doubleValue());
    latLong.set_long(ingest.getAddress().getLongitude().doubleValue());
    modelCase.setLocation(latLong);

    modelCase.setHtc(0);
    modelCase.setPriority(0);
    modelCase.setDescription("Census");
    modelCase.setSpecialInstructions("special instructions");
    modelCase.setHoldUntil(instant.toString());

    return modelCase;
  }
}

package uk.gov.ons.fwmt.census.jobservice.converter.impl;

import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.census.jobservice.dto.Address;
import uk.gov.ons.fwmt.census.jobservice.dto.Contact;
import uk.gov.ons.fwmt.census.jobservice.dto.LatLong;
import uk.gov.ons.fwmt.census.jobservice.dto.ModelCase;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

import java.time.Instant;

import static uk.gov.ons.fwmt.census.jobservice.dto.ModelCase.StateEnum.OPEN;
import static uk.gov.ons.fwmt.census.jobservice.utils.JobServiceUtility.addAddressLines;

@Component("HH")
public class HouseholdConverter implements CometConverter {

  @Override
  public ModelCase convert(FWMTCreateJobRequest ingest) {
    ModelCase modelCase = new ModelCase();
    Instant instant = Instant.now();
    modelCase.setId(ingest.getJobIdentity());
    modelCase.setReference("string");
    modelCase.setCaseType(ingest.getSurveyType());
    modelCase.setState(OPEN);
    modelCase.setCategory("string");
    modelCase.setEstabType("string");
    modelCase.setCoordCode("string");

    Contact contact = new Contact();
    contact.setName("name");
    modelCase.setContact(contact);

    Address address = new Address();
    address.setUprn(0l);
    address.setLines(addAddressLines(ingest));
    address.setPostCode("string");
    modelCase.setAddress(address);

    LatLong latLong = new LatLong();
    latLong.setLat(123456d);
    latLong.set_long(123456d);
    modelCase.setLocation(latLong);

    modelCase.setHtc(0);
    modelCase.setPriority(0);
    modelCase.setDescription("string");
    modelCase.setSpecialInstructions("string");
    modelCase.setHoldUntil(instant.toString());
    return modelCase;
  }
}

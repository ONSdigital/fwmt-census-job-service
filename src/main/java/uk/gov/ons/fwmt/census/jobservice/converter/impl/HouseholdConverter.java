package uk.gov.ons.fwmt.census.jobservice.converter.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.census.jobservice.dto.Address;
import uk.gov.ons.fwmt.census.jobservice.dto.Contact;
import uk.gov.ons.fwmt.census.jobservice.dto.LatLong;
import uk.gov.ons.fwmt.census.jobservice.dto.ModelCase;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

import java.util.ArrayList;
import java.util.List;

import static uk.gov.ons.fwmt.census.jobservice.dto.ModelCase.StateEnum.OPEN;

@Component("HH")
public class HouseholdConverter implements CometConverter {

  @Override
  public ModelCase convert(FWMTCreateJobRequest ingest) {
    ModelCase modelCase = new ModelCase();
    modelCase.setId(ingest.getJobIdentity());
    //    modelCase.setReference();
    modelCase.caseType("HH");
    modelCase.setState(OPEN);
    //    modelCase.setCategory();
    //    modelCase.setEstabType();
    //    modelCase.setCoordCode();

    Contact contact = new Contact();
    contact.setName(ingest.getContact().getForename() + ingest.getContact().getSurname());
    modelCase.setContact(contact);

    Address address = new Address();
    //    address.setUprn();
    address.setLines(addAddressLines(ingest));
    modelCase.setAddress(address);

    LatLong latLong = new LatLong();
    latLong.setLat(ingest.getAddress().getLatitude().doubleValue());
    latLong.setLong(ingest.getAddress().getLongitude().doubleValue());
    modelCase.setLocation(latLong);

    //    modelCase.setHtc();
    //    modelCase.setPriority();
    //    modelCase.setDescription();
    //    modelCase.setSpecialInstructions();
    //    modelCase.setHoldUntil();

    return modelCase;
  }

  private String addAddressLine(List<String> addressLines, String addressLine) {
    if (StringUtils.isNotBlank((addressLine))) {
      addressLines.add(addressLine);
    }
    return addressLine;
  }

  private List<String> addAddressLines(FWMTCreateJobRequest ingest) {
    List<String> addressLines = new ArrayList<>();

    addressLines.add(addAddressLine(addressLines, ingest.getAddress().getLine1()));
    addressLines.add(addAddressLine(addressLines, ingest.getAddress().getLine2()));
    addressLines.add(addAddressLine(addressLines, ingest.getAddress().getLine3()));
    addressLines.add(addAddressLine(addressLines, ingest.getAddress().getLine4()));

    return addressLines;
  }
}

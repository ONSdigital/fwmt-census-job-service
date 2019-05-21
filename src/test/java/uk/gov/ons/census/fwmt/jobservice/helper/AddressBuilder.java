package uk.gov.ons.census.fwmt.jobservice.helper;

import uk.gov.ons.census.fwmt.canonical.v1.Address;

import java.math.BigDecimal;

public class AddressBuilder {

  public Address createNewAddress () {
    Address address = new Address();

    address.setLine1("1 Station Road");
    address.setTownName("Town");
    address.setPostCode("AB1 2CD");
    address.setLatitude(BigDecimal.valueOf(1234.56));
    address.setLongitude(BigDecimal.valueOf(2345.67));
    address.setOa("oaTest");

    return address;
  }
}

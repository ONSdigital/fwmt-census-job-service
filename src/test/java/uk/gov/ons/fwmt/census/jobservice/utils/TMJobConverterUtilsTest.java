package uk.gov.ons.fwmt.census.jobservice.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import uk.gov.ons.fwmt.census.canonical.v1.Address;
import uk.gov.ons.fwmt.census.canonical.v1.Contact;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.data.modelcase.ModelCase;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.converter.impl.CCSConverter;
import uk.gov.ons.fwmt.census.jobservice.converter.impl.HouseholdConverter;

public class TMJobConverterUtilsTest {

  @Test
  public void createHHJobTest() {
    CreateFieldWorkerJobRequest ingest = new CreateFieldWorkerJobRequest();
    Address address = new Address();
    Contact contact = new Contact();
    ingest.setActionType("Create");
    ingest.setJobIdentity("1234");
    ingest.setSurveyType("HH");
    ingest.setPreallocatedJob(true);
    ingest.setMandatoryResourceAuthNo("1234");
    ingest.setDueDate(LocalDate.parse("2018-08-16"));
    address.setLine1("886");
    address.setLine2("Prairie Rose");
    address.setLine3("Trail");
    address.setLine4("RU");
    address.setTownName("Borodinskiy");
    address.setPostCode("188961");
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));
    contact.setPhoneNumber("1234567890");
    contact.setEmail("test@test.com");
    contact.setForename("test");
    contact.setSurname("test");
    ingest.setAddress(address);
    ingest.setContact(contact);
    ingest.setCaseId(UUID.randomUUID());
    Map<String, String> additionalProperties = new HashMap<>();
    ingest.setAdditionalProperties(additionalProperties);

    HouseholdConverter converter = new HouseholdConverter();
    ModelCase request = converter.convert(ingest);

    assertNotNull(request);
  }

  @Test
  public void createCCSJobTest() throws GatewayException {
    String user = "bob.smith";
    CreateFieldWorkerJobRequest ingest = new CreateFieldWorkerJobRequest();
    Address address = new Address();
    Contact contact = new Contact();
    ingest.setActionType("Create");
    ingest.setJobIdentity("1234");
    ingest.setSurveyType("CCS");
    ingest.setPreallocatedJob(true);
    ingest.setMandatoryResourceAuthNo("1234");
    ingest.setDueDate(LocalDate.parse("2018-08-16"));
    address.setLine1("886");
    address.setLine2("Prairie Rose");
    address.setLine3("Trail");
    address.setLine4("RU");
    address.setTownName("Borodinskiy");
    address.setPostCode("188961");
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));
    contact.setPhoneNumber("1234567890");
    contact.setEmail("test@test.com");
    contact.setForename("test");
    contact.setSurname("test");
    ingest.setAddress(address);
    ingest.setContact(contact);
    ingest.setCaseId(UUID.randomUUID());
    Map<String, String> additionalProperties = new HashMap<>();
    ingest.setAdditionalProperties(additionalProperties);

    CCSConverter converter = new CCSConverter();
    ModelCase request = converter.convert(ingest);

    assertEquals("1234", request.getReference());
    assertEquals("188961", request.getContact().getName());
  }

}

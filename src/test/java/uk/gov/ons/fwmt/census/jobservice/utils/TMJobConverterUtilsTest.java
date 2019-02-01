package uk.gov.ons.fwmt.census.jobservice.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.junit.Test;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;

import uk.gov.ons.fwmt.census.canonical.v1.Address;
import uk.gov.ons.fwmt.census.canonical.v1.Contact;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
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
    Map<String, String> additionalProperties = new HashMap();
    additionalProperties.put("caseId", UUID.randomUUID().toString());
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
    Map<String, String> additionalProperties = new HashMap();
    additionalProperties.put("caseId", UUID.randomUUID().toString());
    ingest.setAdditionalProperties(additionalProperties);

    CCSConverter converter = new CCSConverter();
    ModelCase request = converter.convert(ingest);

    assertEquals("1234", request.getReference());
    assertEquals("188961", request.getContact().getName());
  }

  @Test
  public void addAddressLinesTest() throws DatatypeConfigurationException {
    SendCreateJobRequestMessage message = new CreateJobBuilder(DatatypeFactory.newInstance())
        .addAddressLine("number")
        .addAddressLine("street")
        .addAddressLine("town")
        .addAddressLine("city")
        .build();

    LocationType address = message.getCreateJobRequest().getJob().getLocation();

    assertEquals(4, address.getAddressDetail().getLines().getAddressLine().size());
  }

  @Test
  public void checkNumberOfAddressLinesTest() throws DatatypeConfigurationException {
    SendCreateJobRequestMessage message1 = new CreateJobBuilder(DatatypeFactory.newInstance())
        .addAddressLine("number")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("town")
        .addAddressLine("city")
        .build();

    LocationType address1 = message1.getCreateJobRequest().getJob().getLocation();

    assertEquals(6, address1.getAddressDetail().getLines().getAddressLine().size());

    SendCreateJobRequestMessage message2 = new CreateJobBuilder(DatatypeFactory.newInstance())
        .addAddressLine("number")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("street")
        .addAddressLine("town")
        .addAddressLine("city")
        .shrinkAddressLines()
        .build();

    LocationType address2 = message2.getCreateJobRequest().getJob().getLocation();

    assertEquals(5, address2.getAddressDetail().getLines().getAddressLine().size());
  }

  @Test
  public void deleteJobTest() {
    SendDeleteJobRequestMessage request = TMJobConverterUtils.deleteJob("1234", "wrong address", "admin");

    assertEquals("wrong address", request.getDeleteJobRequest().getDeletionReason());
    assertEquals("1234", request.getDeleteJobRequest().getIdentity().getReference());
  }
}

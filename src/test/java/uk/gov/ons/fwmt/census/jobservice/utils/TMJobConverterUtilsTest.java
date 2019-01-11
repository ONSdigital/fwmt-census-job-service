package uk.gov.ons.fwmt.census.jobservice.utils;

import com.consiliumtechnologies.schemas.mobile._2015._05.optimisetypes.LocationType;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendDeleteJobRequestMessage;
import org.junit.jupiter.api.Test;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.converter.impl.CCSConverter;
import uk.gov.ons.fwmt.census.jobservice.converter.impl.HouseholdConverter;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TMJobConverterUtilsTest {
  DatatypeFactory datatypeFactory;

  public TMJobConverterUtilsTest() throws DatatypeConfigurationException {
    datatypeFactory = DatatypeFactory.newInstance();
  }

  @Test
  public void createHHJobTest() {
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    Address address = new Address();
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
    ingest.setAddress(address);

    HouseholdConverter converter = new HouseholdConverter();
    ModelCase request = converter.convert(ingest);

    assertNotNull(request);
  }

  @Test
  public void createCCSJobTest() throws CTPException {
    String user = "bob.smith";
    FWMTCreateJobRequest ingest = new FWMTCreateJobRequest();
    Address address = new Address();
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
    ingest.setAddress(address);

    CCSConverter converter = new CCSConverter("Default", "Mod", 0);
    SendCreateJobRequestMessage request = converter.convert(ingest);

    assertEquals("1234", request.getCreateJobRequest().getJob().getIdentity().getReference());
    assertEquals("188961", request.getCreateJobRequest().getJob().getContact().getName());
    assertEquals(datatypeFactory.newXMLGregorianCalendar("2018-08-16T23:59:59.000Z"),
        request.getCreateJobRequest().getJob().getDueDate());
    assertEquals("Census - 188961", request.getCreateJobRequest().getJob().getDescription());

    assertEquals("\\OPTIMISE\\INPUT", request.getSendMessageRequestInfo().getQueueName());
    assertEquals("1234", request.getSendMessageRequestInfo().getKey());
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

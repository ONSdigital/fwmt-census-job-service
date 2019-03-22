package uk.gov.ons.census.fwmt.jobservice.utils;

import org.junit.Test;
import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.Contact;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Pause;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.jobservice.converter.impl.HouseholdConverter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class TMJobConverterUtilsTest {

  @Test
  public void createHHJobTest() {
    CreateFieldWorkerJobRequest ingest = new CreateFieldWorkerJobRequest();
    Address address = new Address();
    Contact contact = new Contact();
    Pause pause = new Pause();
    ingest.setActionType("Create");
    ingest.setSurveyType("HH");
    address.setLine1("886");
    address.setLine2("Prairie Rose");
    address.setLine3("Trail");
    address.setLine4("RU");
    address.setTownName("Borodinskiy");
    address.setPostCode("188961");
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));
    contact.setPhoneNumber("1234567890");
    contact.setForename("test");
    contact.setSurname("test");
    pause.setHoldUntil(OffsetDateTime.now());
    pause.setReason("reason");
    //    pause.setCode("code");
    //    Date date = new Date();
    //    pause.setEffectiveDate(date);
    ingest.setAddress(address);
    ingest.setContact(contact);
    ingest.setPause(pause);
    ingest.setCaseId(UUID.randomUUID());
    
    HouseholdConverter converter = new HouseholdConverter();
    CaseRequest request = converter.convert(ingest);

    assertNotNull(request);
  }
}

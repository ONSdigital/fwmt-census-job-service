package uk.gov.ons.census.fwmt.jobservice.helper;

import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Contact;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class FieldWorkerJobRequestBuilder {

  public CreateFieldWorkerJobRequest createFieldWorkerJobRequestForConvert() {
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new CreateFieldWorkerJobRequest();

    createFieldWorkerJobRequest.setActionType("Create");
    createFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    createFieldWorkerJobRequest.setCaseReference("caseReference");
    createFieldWorkerJobRequest.setCaseType("Household");
    createFieldWorkerJobRequest.setEstablishmentType("estabType");
    createFieldWorkerJobRequest.setCoordinatorId("coordId");

    createFieldWorkerJobRequest.setAddress(createNewAddress());

    Contact contact = new Contact();

    contact.setOrganisationName("Test");
    contact.setEmailAddress("Test@test.co.uk");
    contact.setPhoneNumber("07123456789");
    createFieldWorkerJobRequest.setContact(contact);

    createFieldWorkerJobRequest.setMandatoryResource("TestMand");

    return createFieldWorkerJobRequest;
  }

  public CancelFieldWorkerJobRequest cancelFieldWorkerJobRequest() {
    CancelFieldWorkerJobRequest cancelFieldWorkerJobRequest = new CancelFieldWorkerJobRequest();

    cancelFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    cancelFieldWorkerJobRequest.setReason("TestReason");
    cancelFieldWorkerJobRequest.setActionType("actionType");
    cancelFieldWorkerJobRequest.setUntil(OffsetDateTime.parse("2030-01-01T00:00+00:00"));

    return cancelFieldWorkerJobRequest;
  }

  private Address createNewAddress() {
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

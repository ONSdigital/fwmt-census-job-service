package uk.gov.ons.census.fwmt.jobservice.helper;

import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Contact;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;

import java.time.OffsetDateTime;
import java.util.UUID;

public class FieldWorkerJobRequestBuilder {

  public CreateFieldWorkerJobRequest createFieldWorkerJobRequestForConvert() {
    AddressBuilder addressBuilder = new AddressBuilder();
    Contact contact = new Contact();
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new CreateFieldWorkerJobRequest();

    createFieldWorkerJobRequest.setActionType("Create");
    createFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    createFieldWorkerJobRequest.setCaseReference("caseReference");
    createFieldWorkerJobRequest.setCaseType("Household");
    createFieldWorkerJobRequest.setEstablishmentType("estabType");
    createFieldWorkerJobRequest.setCoordinatorId("coordId");

    Address address = addressBuilder.createNewAddress();
    createFieldWorkerJobRequest.setAddress(address);

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
}

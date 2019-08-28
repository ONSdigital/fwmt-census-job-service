package uk.gov.ons.census.fwmt.jobservice.helper;

import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Contact;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;

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

  public CreateFieldWorkerJobRequest createFieldWorkerJobRequestForConvertWithoutContact() {
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new CreateFieldWorkerJobRequest();


    createFieldWorkerJobRequest.setActionType("Create");
    createFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    createFieldWorkerJobRequest.setCaseReference("caseReference");
    createFieldWorkerJobRequest.setCaseType("Household");
    createFieldWorkerJobRequest.setEstablishmentType("estabType");
    createFieldWorkerJobRequest.setCoordinatorId("coordId");

    createFieldWorkerJobRequest.setAddress(createNewAddress());

    createFieldWorkerJobRequest.setMandatoryResource("TestMand");

    return createFieldWorkerJobRequest;
  }

  public CreateFieldWorkerJobRequest createFieldWorkerCEJobRequestForConvert() {
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new CreateFieldWorkerJobRequest();

    createFieldWorkerJobRequest.setActionType("Create");
    createFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    createFieldWorkerJobRequest.setCaseReference("caseReference");
    createFieldWorkerJobRequest.setCaseType("CE");
    createFieldWorkerJobRequest.setEstablishmentType("estabType");
    createFieldWorkerJobRequest.setCoordinatorId("coordId");

    createFieldWorkerJobRequest.setAddress(createNewAddress());

    createFieldWorkerJobRequest.setMandatoryResource("TestMand");

    return createFieldWorkerJobRequest;
  }

  public CreateFieldWorkerJobRequest createFieldWorkerCCSIVJobRequestForConvert() {
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new CreateFieldWorkerJobRequest();

    createFieldWorkerJobRequest.setActionType("Create");
    createFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    createFieldWorkerJobRequest.setCaseReference("caseReference");
    createFieldWorkerJobRequest.setCaseType("CCS");
    createFieldWorkerJobRequest.setCoordinatorId("coordId");
    createFieldWorkerJobRequest.setMandatoryResource("ccsInterviewer");

    createFieldWorkerJobRequest.setAddress(createCCSNewAddress());

    createFieldWorkerJobRequest.setMandatoryResource("TestMand");

    return createFieldWorkerJobRequest;
  }

  public CreateFieldWorkerJobRequest createFieldWorkerCCSPLJobRequestForConvert() {
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new CreateFieldWorkerJobRequest();

    createFieldWorkerJobRequest.setActionType("Create");
    createFieldWorkerJobRequest.setCaseReference("123456789");
    createFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    createFieldWorkerJobRequest.setCoordinatorId("coordId");
    createFieldWorkerJobRequest.setMandatoryResource("ccsInterviewer");

    createFieldWorkerJobRequest.setAddress(createCCSNewAddress());

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

  private Address createCCSNewAddress() {
    Address address = new Address();

    address.setPostCode("AB1 2CD");
    address.setLatitude(BigDecimal.valueOf(1234.56));
    address.setLongitude(BigDecimal.valueOf(2345.67));

    return address;
  }

  public UpdateFieldWorkerJobRequest updateFieldWorkerJobRequestWithPause() {
    UpdateFieldWorkerJobRequest updateFieldWorkerJobRequest = new UpdateFieldWorkerJobRequest();

    updateFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    updateFieldWorkerJobRequest.setAddressType("HH");
    updateFieldWorkerJobRequest.setActionType("update");
    updateFieldWorkerJobRequest.setUaa(false);
    updateFieldWorkerJobRequest.setHoldUntil(OffsetDateTime.parse("2019-07-27T00:00+00:00"));
    updateFieldWorkerJobRequest.setBlankFormReturned(false);

    return updateFieldWorkerJobRequest;
  }

  public UpdateFieldWorkerJobRequest updateFieldWorkerJobRequestReinstate() {
    UpdateFieldWorkerJobRequest updateFieldWorkerJobRequest = new UpdateFieldWorkerJobRequest();

    updateFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    updateFieldWorkerJobRequest.setAddressType("HH");
    updateFieldWorkerJobRequest.setActionType("update");
    updateFieldWorkerJobRequest.setUaa(false);
    updateFieldWorkerJobRequest.setHoldUntil(OffsetDateTime.parse("2019-05-26T00:00+00:00"));
    updateFieldWorkerJobRequest.setBlankFormReturned(true);

    return updateFieldWorkerJobRequest;
  }

  public CreateFieldWorkerJobRequest createFieldWorkerJobRequestForCCSIVConvert() {
    Address address = new Address();
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new CreateFieldWorkerJobRequest();

    createFieldWorkerJobRequest.setActionType("Create");
    createFieldWorkerJobRequest.setCaseReference("Test");
    createFieldWorkerJobRequest.setSurveyType("CCSIV");
    createFieldWorkerJobRequest.setEstablishmentType("Household");
    createFieldWorkerJobRequest.setMandatoryResource("Test_123");
    createFieldWorkerJobRequest.setCoordinatorId("123_abc");

    address.setOa("!23_abc");
    address.setLine1("1");
    address.setLine2("Station Road");
    address.setTownName("Chingford");
    address.setPostCode("E4 7NG");
    address.setLatitude(BigDecimal.valueOf(51));
    address.setLongitude(BigDecimal.valueOf(0.0001));
    createFieldWorkerJobRequest.setAddress(address);

    createFieldWorkerJobRequest.setCaseId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    createFieldWorkerJobRequest.setCaseReference("caseReference");
    createFieldWorkerJobRequest.setCaseType("Household");
    createFieldWorkerJobRequest.setCcsQuestionnaireURL("1133244");
    createFieldWorkerJobRequest.setSpecialInstructions("Test");

    createFieldWorkerJobRequest.setAddress(createNewAddress());

    Contact contact = new Contact();

    contact.setOrganisationName("Test");
    contact.setEmailAddress("Test@test.co.uk");
    contact.setPhoneNumber("07123456789");
    createFieldWorkerJobRequest.setContact(contact);

    return createFieldWorkerJobRequest;
  }


}

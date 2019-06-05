package uk.gov.ons.census.fwmt.jobservice.converter;

import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.converter.impl.HouseholdConverter;
import uk.gov.ons.census.fwmt.jobservice.helper.FieldWorkerJobRequestBuilder;
import uk.gov.ons.census.fwmt.jobservice.rest.client.CometRestClient;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class HouseHoldConverterTest {

  @InjectMocks
  HouseholdConverter householdConverter;

  @Mock
  private CometRestClient cometRestClient;

  @Mock
  private ModelCase modelCase;

  @Mock
  private MapperFacade mapperFacade;

  @Mock
  private CaseRequest caseRequest;

  @Test
  public void createConvertRequest() {
    // Given
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .createFieldWorkerJobRequestForConvert();

    // When
    CaseRequest caseRequest = householdConverter.convert(createFieldWorkerJobRequest);

    // Then
    assertEquals(createFieldWorkerJobRequest.getCaseReference(), caseRequest.getReference());
    assertEquals("HH", caseRequest.getType().toString());
    assertEquals(createFieldWorkerJobRequest.getMandatoryResource(), caseRequest.getFieldOfficerId());

  }

  @Test
  public void createConvertPause() {
    // Given
    CancelFieldWorkerJobRequest cancelFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .cancelFieldWorkerJobRequest();

    // When
    CasePauseRequest casePauseRequest = householdConverter.convertCancel(cancelFieldWorkerJobRequest);

    // Then
    assertEquals(cancelFieldWorkerJobRequest.getCaseId().toString(), casePauseRequest.getId());
    assertEquals(cancelFieldWorkerJobRequest.getUntil(), casePauseRequest.getUntil());
  }

  @Test
  public void createConvertUpdate() throws GatewayException {
    // Given
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .createFieldWorkerJobRequestForConvert();

    CaseRequest caseRequest = householdConverter.convert(createFieldWorkerJobRequest);

    UpdateFieldWorkerJobRequest updateFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .updateFieldWorkerJobRequest();

    ModelCase modelCase = new ModelCase();
    modelCase.setId(UUID.fromString("a48bf28e-e7f4-4467-a9fb-e000b6a55676"));
    modelCase.setType(ModelCase.TypeEnum.HH);
    modelCase.setAddress(caseRequest.getAddress());
    modelCase.setCategory(caseRequest.getCategory());
    modelCase.setCoordCode(caseRequest.getCoordCode());
    modelCase.setContact(caseRequest.getContact());
    modelCase.setDescription(caseRequest.getDescription());
    modelCase.setEstabType(caseRequest.getEstabType());
    modelCase.setLocation(caseRequest.getLocation());

    // When
    CaseRequest caseUpdateRequest = householdConverter.convertUpdate(updateFieldWorkerJobRequest, modelCase);

    // Then
    assertEquals(updateFieldWorkerJobRequest.getId().toString(), caseUpdateRequest.getPause().getId());
    assertEquals(updateFieldWorkerJobRequest.getUntil(), caseUpdateRequest.getPause().getUntil());
  }
}

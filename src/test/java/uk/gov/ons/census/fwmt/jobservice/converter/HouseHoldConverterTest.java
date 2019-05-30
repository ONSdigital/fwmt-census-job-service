package uk.gov.ons.census.fwmt.jobservice.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.jobservice.converter.impl.HouseholdConverter;
import uk.gov.ons.census.fwmt.jobservice.helper.FieldWorkerJobRequestBuilder;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class HouseHoldConverterTest {

  @InjectMocks
  HouseholdConverter householdConverter;

  @Test
  public void createConvertRequestTest() {
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
  public void createConvertCancelTest() {
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
  public void createConvertUpdateTest() {
    // Given
    UpdateFieldWorkerJobRequest updateFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .updateFieldWorkerJobRequest();

    // When
    CasePauseRequest casePauseRequest = householdConverter.convertUpdate(updateFieldWorkerJobRequest);

    // Then
    assertEquals(updateFieldWorkerJobRequest.getCaseId().toString(), casePauseRequest.getId());
    assertEquals(updateFieldWorkerJobRequest.getUntil(), casePauseRequest.getUntil());
  }
}

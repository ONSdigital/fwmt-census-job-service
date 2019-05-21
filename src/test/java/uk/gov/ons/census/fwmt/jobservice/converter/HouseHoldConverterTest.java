package uk.gov.ons.census.fwmt.jobservice.converter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
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
  public void createConvertRequest() {
    // Given
    FieldWorkerJobRequestBuilder fieldWorkerJobRequestBuilder = new FieldWorkerJobRequestBuilder();
    fieldWorkerJobRequestBuilder.createFieldWorkerJobRequestForConvert();

    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = fieldWorkerJobRequestBuilder.createFieldWorkerJobRequestForConvert();

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
    FieldWorkerJobRequestBuilder fieldWorkerJobRequestBuilder = new FieldWorkerJobRequestBuilder();
    fieldWorkerJobRequestBuilder.cancelFieldWorkerJobRequest();

    CancelFieldWorkerJobRequest cancelFieldWorkerJobRequest = fieldWorkerJobRequestBuilder.cancelFieldWorkerJobRequest();

    // When
    CasePauseRequest casePauseRequest = householdConverter.convertPause(cancelFieldWorkerJobRequest);

    // Then
    assertEquals(cancelFieldWorkerJobRequest.getCaseId().toString(), casePauseRequest.getId());
    assertEquals(cancelFieldWorkerJobRequest.getUntil(), casePauseRequest.getUntil());
  }
}

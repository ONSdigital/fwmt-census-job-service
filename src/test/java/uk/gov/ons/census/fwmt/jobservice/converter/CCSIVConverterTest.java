package uk.gov.ons.census.fwmt.jobservice.converter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingCached;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.converter.impl.CCSIVConverter;
import uk.gov.ons.census.fwmt.jobservice.entity.CCSOutcomeStore;
import uk.gov.ons.census.fwmt.jobservice.helper.CCSPropertyListedCachedBuilder;
import uk.gov.ons.census.fwmt.jobservice.helper.FieldWorkerJobRequestBuilder;
import uk.gov.ons.census.fwmt.jobservice.message.MessageConverter;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class CCSIVConverterTest {

  @InjectMocks
  private CCSIVConverter ccsivConverter;

  @Mock
  private CCSOutcomeStore ccsOutcomeStore;

  @Mock
  private MessageConverter messageConverter;

  @Mock
  private ObjectMapper objectMapper;

  @Test
  public void createConvertRequest() throws GatewayException {
    // Given
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .createFieldWorkerCCSIVJobRequestForConvert();

    CCSPropertyListingCached ccsPropertyListingCached = new CCSPropertyListedCachedBuilder()
            .createCCSPropertyListedCache();

    String caseId = createFieldWorkerJobRequest.getCaseId().toString();

    String output = "Any";

    Mockito.when(ccsOutcomeStore.retrieveCache(caseId)).thenReturn(output);

    Mockito.when(messageConverter.convertMessageToDTO(any(), anyString()))
            .thenReturn(ccsPropertyListingCached);

    // When
    CaseRequest caseRequest = ccsivConverter.convert(createFieldWorkerJobRequest);

    // Then
    assertEquals(createFieldWorkerJobRequest.getCaseReference(), caseRequest.getReference());
    assertEquals("CCSIV", caseRequest.getType().toString());
    assertEquals(createFieldWorkerJobRequest.getMandatoryResource(), caseRequest.getRequiredOfficer());

  }
}
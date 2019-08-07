package uk.gov.ons.census.fwmt.jobservice.converter;

import ma.glasnost.orika.MapperFacade;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.converter.impl.CCSIVConverter;
import uk.gov.ons.census.fwmt.jobservice.converter.impl.CEConverter;
import uk.gov.ons.census.fwmt.jobservice.helper.FieldWorkerJobRequestBuilder;
import uk.gov.ons.census.fwmt.jobservice.rest.client.CometRestClient;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CCSIVConverterTest {

  @InjectMocks
  private CCSIVConverter ccsivConverter;

  @Mock
  private CometRestClient cometRestClient;

  @Mock
  private ModelCase modelCase;

  @Mock
  private MapperFacade mapperFacade;

  @Mock
  private CaseRequest caseRequest;

  @Test
  public void createConvertRequest() throws GatewayException {
    // Given
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .createFieldWorkerCEJobRequestForConvert();

    // When
    CaseRequest caseRequest = ccsivConverter.convert(createFieldWorkerJobRequest);

    // Then
    assertEquals(createFieldWorkerJobRequest.getCaseReference(), caseRequest.getReference());
    assertEquals("CCSIV", caseRequest.getType().toString());
    assertEquals(createFieldWorkerJobRequest.getMandatoryResource(), caseRequest.getRequiredOfficer());

  }
}

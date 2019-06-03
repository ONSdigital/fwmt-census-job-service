package uk.gov.ons.census.fwmt.jobservice.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.converter.impl.HouseholdConverter;
import uk.gov.ons.census.fwmt.jobservice.helper.FieldWorkerJobRequestBuilder;
import uk.gov.ons.census.fwmt.jobservice.rest.client.CometRestClient;

@RunWith(MockitoJUnitRunner.class)
public class CometRestClientTest {

  @Mock
  private CometRestClient cometRestClient;

  @Mock
  private HouseholdConverter householdConverter;

  @Test
  public void sendRequestTest() throws GatewayException {
    // Given
    CreateFieldWorkerJobRequest createFieldWorkerJobRequest = new FieldWorkerJobRequestBuilder()
        .createFieldWorkerJobRequestForConvert();

    // When
    CaseRequest caseRequest = householdConverter.convert(createFieldWorkerJobRequest);

    cometRestClient.sendRequest(caseRequest, "a48bf28e-e7f4-4467-a9fb-e000b6a55676");

    // Then
    Mockito.verify(cometRestClient).sendRequest(caseRequest, "a48bf28e-e7f4-4467-a9fb-e000b6a55676");
  }
}

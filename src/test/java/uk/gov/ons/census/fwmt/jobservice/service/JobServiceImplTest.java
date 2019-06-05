package uk.gov.ons.census.fwmt.jobservice.service;

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
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;
import uk.gov.ons.census.fwmt.jobservice.helper.FieldWorkerJobRequestBuilder;
import uk.gov.ons.census.fwmt.jobservice.rest.client.CometRestClient;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CANCEL_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CANCEL_SENT;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_SENT;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_UPDATE_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_UPDATE_SENT;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceImplTest {

  @InjectMocks
  private JobServiceImpl jobServiceImpl;

  @Mock
  private GatewayEventManager gatewayEventManager;

  @Mock
  private Map<String, CometConverter> cometConverters;

  @Mock
  private CometRestClient restClient;

  @Mock
  private CometConverter cometConverter;

  @Test
  public void createConvertAndSendCreateTest() throws GatewayException {
    // Given
    CreateFieldWorkerJobRequest jobRequest = new FieldWorkerJobRequestBuilder().createFieldWorkerJobRequestForConvert();

    CaseRequest caseRequest = new CaseRequest();

    // When
    when(cometConverters.get(jobRequest.getCaseType())).thenReturn(cometConverter);
    when(cometConverter.convert(any(CreateFieldWorkerJobRequest.class))).thenReturn(caseRequest);

    jobServiceImpl.createJob(jobRequest);

    // Then
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(COMET_CREATE_SENT), any());
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(COMET_CREATE_ACK), any());

  }

  @Test
  public void pauseConvertAndSendCancelTest() throws GatewayException {
    // Given
    CancelFieldWorkerJobRequest jobRequest = new FieldWorkerJobRequestBuilder().cancelFieldWorkerJobRequest();

    CasePauseRequest casePauseRequest = new CasePauseRequest();

    // When
    when(cometConverters.get("Household")).thenReturn(cometConverter);
    when(cometConverter.convertCancel(any(CancelFieldWorkerJobRequest.class))).thenReturn(casePauseRequest);

    jobServiceImpl.cancelJob(jobRequest);

    // Then
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(COMET_CANCEL_SENT), any());
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(COMET_CANCEL_ACK), any());

  }

  @Test
  public void updateConvertAndSendUpdateTest() throws GatewayException {
    // Given
    UpdateFieldWorkerJobRequest jobRequest = new FieldWorkerJobRequestBuilder().updateFieldWorkerJobRequest();

    CaseRequest caseRequest = new CaseRequest();

    // When
    when(cometConverters.get("Household")).thenReturn(cometConverter);
    when(cometConverter.convertUpdate(any(UpdateFieldWorkerJobRequest.class), any(ModelCase.class))).thenReturn(caseRequest);

    jobServiceImpl.convertAndSendUpdate(jobRequest);

    // Then
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(COMET_UPDATE_SENT), any());
    Mockito.verify(gatewayEventManager).triggerEvent(anyString(), eq(COMET_UPDATE_ACK), any());

  }
}

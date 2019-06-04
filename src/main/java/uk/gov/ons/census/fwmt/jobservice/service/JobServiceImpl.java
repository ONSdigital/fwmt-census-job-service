package uk.gov.ons.census.fwmt.jobservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;
import uk.gov.ons.census.fwmt.jobservice.rest.client.CometRestClient;
import uk.gov.ons.census.fwmt.jobservice.service.JobService;

import java.time.LocalTime;
import java.util.Map;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CANCEL_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CANCEL_SENT;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_SENT;

@Service
public class JobServiceImpl implements JobService {
  @Autowired
  private CometRestClient cometRestClient;

  @Autowired
  private Map<String, CometConverter> cometConverters;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Override
  public void createJob(CreateFieldWorkerJobRequest jobRequest) throws GatewayException {
    convertAndSendCreate(jobRequest);
  }

  @Override
  public void cancelJob(CancelFieldWorkerJobRequest cancelRequest) throws GatewayException {
    convertAndSendCancel(cancelRequest);
  }

  @Override
  public void convertAndSendCreate(CreateFieldWorkerJobRequest jobRequest) throws GatewayException {
    final CometConverter cometConverter = cometConverters.get(jobRequest.getCaseType());
    CaseRequest caseRequest = cometConverter.convert(jobRequest);
    gatewayEventManager.triggerEvent(String.valueOf(jobRequest.getCaseId()), COMET_CREATE_SENT, LocalTime.now());
    cometRestClient.sendRequest(caseRequest, String.valueOf(jobRequest.getCaseId()));
    gatewayEventManager.triggerEvent(String.valueOf(jobRequest.getCaseId()), COMET_CREATE_ACK, LocalTime.now());
  }

  public void convertAndSendCancel(CancelFieldWorkerJobRequest cancelJobRequest) throws GatewayException {
    final CometConverter cometConverter = cometConverters.get("Household");
    CasePauseRequest casePauseRequest = cometConverter.convertPause(cancelJobRequest);
    gatewayEventManager.triggerEvent(String.valueOf(cancelJobRequest.getCaseId()), COMET_CANCEL_SENT, LocalTime.now());
    cometRestClient.sendRequest(casePauseRequest, String.valueOf(cancelJobRequest.getCaseId()));
    gatewayEventManager.triggerEvent(String.valueOf(cancelJobRequest.getCaseId()), COMET_CANCEL_ACK, LocalTime.now());
  }

}

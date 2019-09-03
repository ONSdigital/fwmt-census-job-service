package uk.gov.ons.census.fwmt.jobservice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;
import uk.gov.ons.census.fwmt.jobservice.rest.client.CometRestClient;
import uk.gov.ons.census.fwmt.jobservice.service.JobService;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CANCEL_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CANCEL_SENT;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_SENT;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_UPDATE_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_UPDATE_SENT;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.FAILED_TO_CREATE_TM_JOB;;

@Service
public class JobServiceImpl implements JobService {
  @Autowired
  private CometRestClient cometRestClient;

  @Autowired
  private Map<String, CometConverter> cometConverters;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  private static final List<HttpStatus> validResponses = List.of(HttpStatus.OK, HttpStatus.CREATED, HttpStatus.ACCEPTED);

  
  @Override
  public void createJob(CreateFieldWorkerJobRequest jobRequest) throws GatewayException {
    convertAndSendCreate(jobRequest);
  }

  @Override
  public void cancelJob(CancelFieldWorkerJobRequest cancelRequest) throws GatewayException {
    convertAndSendCancel(cancelRequest);
  }

  @Override
  public void updateJob(UpdateFieldWorkerJobRequest updateRequest) throws GatewayException {
    convertAndSendUpdate(updateRequest);
  }

  @Override
  public void convertAndSendCreate(CreateFieldWorkerJobRequest jobRequest) throws GatewayException {
    final CometConverter cometConverter = cometConverters.get(jobRequest.getCaseType());
    CaseRequest caseRequest = cometConverter.convert(jobRequest);
    gatewayEventManager.triggerEvent(String.valueOf(jobRequest.getCaseId()), COMET_CREATE_SENT);
    ResponseEntity<Void> response = cometRestClient.sendRequest(caseRequest, String.valueOf(jobRequest.getCaseId()));
    if (!isValidResponse(response)) {
      String msg = "Unable to Create FieldWorkerJobRequest: HTTP_STATUS:" + response.getStatusCode() + ":" + response.getStatusCodeValue();
      gatewayEventManager.triggerErrorEvent(this.getClass(), msg, String.valueOf(jobRequest.getCaseId()), FAILED_TO_CREATE_TM_JOB);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, msg);
    }
    gatewayEventManager.triggerEvent(String.valueOf(jobRequest.getCaseId()), COMET_CREATE_ACK);
  }

  public void convertAndSendCancel(CancelFieldWorkerJobRequest cancelJobRequest) throws GatewayException {
    final CometConverter cometConverter = cometConverters.get("HH");
    CasePauseRequest casePauseRequest = cometConverter.convertCancel(cancelJobRequest);
    gatewayEventManager.triggerEvent(String.valueOf(cancelJobRequest.getCaseId()), COMET_CANCEL_SENT);
    ResponseEntity<Void> response = cometRestClient.sendRequest(casePauseRequest, String.valueOf(cancelJobRequest.getCaseId()));
    if (!isValidResponse(response)) {
      String msg = "Unable to Create FieldWorkerJobRequest: HTTP_STATUS:" + response.getStatusCode() + ":" + response.getStatusCodeValue();
      gatewayEventManager.triggerErrorEvent(this.getClass(), msg, String.valueOf(cancelJobRequest.getCaseId()), FAILED_TO_CREATE_TM_JOB);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, msg);
    }
    gatewayEventManager.triggerEvent(String.valueOf(cancelJobRequest.getCaseId()), COMET_CANCEL_ACK);
  }

  public void convertAndSendUpdate(UpdateFieldWorkerJobRequest updateRequest) throws GatewayException {
    final CometConverter cometConverter = cometConverters.get("HH");
    ModelCase modelCase = cometRestClient.getCase(String.valueOf(updateRequest.getCaseId()));
    CaseRequest caseRequest = cometConverter.convertUpdate(updateRequest, modelCase);
    gatewayEventManager.triggerEvent(String.valueOf(updateRequest.getCaseId()), COMET_UPDATE_SENT);
    if (!StringUtils.isEmpty(caseRequest.getPause())) {
      CasePauseRequest casePauseRequest = caseRequest.getPause();
      ResponseEntity<Void> response = cometRestClient.sendRequest(casePauseRequest, String.valueOf(updateRequest.getCaseId()));
      if (!isValidResponse(response)) {
        String msg = "Unable to Create FieldWorkerJobRequest: HTTP_STATUS:" + response.getStatusCode() + ":" + response.getStatusCodeValue();
        gatewayEventManager.triggerErrorEvent(this.getClass(), msg, String.valueOf(updateRequest.getCaseId()), FAILED_TO_CREATE_TM_JOB);
        throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, msg);
      }
    }
    ResponseEntity<Void> response = cometRestClient.sendRequest(caseRequest, String.valueOf(updateRequest.getCaseId()));
    if (!isValidResponse(response)) {
      String msg = "Unable to Create FieldWorkerJobRequest: HTTP_STATUS:" + response.getStatusCode() + ":" + response.getStatusCodeValue();
      gatewayEventManager.triggerErrorEvent(this.getClass(), msg, String.valueOf(updateRequest.getCaseId()), FAILED_TO_CREATE_TM_JOB);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, msg);
    }
    gatewayEventManager.triggerEvent(String.valueOf(updateRequest.getCaseId()), COMET_UPDATE_ACK);
  }

  private boolean isValidResponse(ResponseEntity<Void> response) {
    return validResponses.contains(response.getStatusCode());
  }


  
}

package uk.gov.ons.census.fwmt.jobservice.message;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_CANCEL_RECEIVED;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_CREATE_JOB_RECEIVED;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_UPDATE_RECEIVED;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig;
import uk.gov.ons.census.fwmt.jobservice.service.JobService;

@Slf4j
@Component
public class GatewayActionsReceiver {

  @Autowired
  private JobService jobService;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private ObjectMapper mapper;

  public void receiveMessage(String message) throws GatewayException {
    log.info("received a message from RM-Adapter");
    processMessage(message);
  }

  private void processMessage(String message) throws GatewayException {
    if (message.contains("Create")) {
      CreateFieldWorkerJobRequest fwmtCreateJobRequest = convertMessageToDTO(CreateFieldWorkerJobRequest.class, message);
      gatewayEventManager.triggerEvent(String.valueOf(fwmtCreateJobRequest.getCaseId()), CANONICAL_CREATE_JOB_RECEIVED);
      jobService.createJob(fwmtCreateJobRequest);
    } else if (message.contains("Cancel")) {
      CancelFieldWorkerJobRequest fwmtCancelJobRequest = convertMessageToDTO(CancelFieldWorkerJobRequest.class, message);
      gatewayEventManager.triggerEvent(String.valueOf(fwmtCancelJobRequest.getCaseId()), CANONICAL_CANCEL_RECEIVED);
      jobService.cancelJob(fwmtCancelJobRequest);
    } else if (message.contains("update")) {
      UpdateFieldWorkerJobRequest fwmtUpdateJobRequest = convertMessageToDTO(UpdateFieldWorkerJobRequest.class, message);
      gatewayEventManager.triggerEvent(String.valueOf(fwmtUpdateJobRequest.getCaseId()), CANONICAL_UPDATE_RECEIVED);
      jobService.updateJob(fwmtUpdateJobRequest);
    } else {
      String errorMsg = "Invalid Canonical Action.";
      gatewayEventManager.triggerErrorEvent(this.getClass(), errorMsg, "<UNKNOWN_CASE_ID>", GatewayEventsConfig.INVALID_CANONICAL_ACTION);
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, errorMsg);
    }
  }

  private <T> T convertMessageToDTO(Class<T> klass, String message) throws GatewayException {
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    T dto;
    try {
      dto = mapper.readValue(message, klass);
    } catch (IOException e) {
      String errorMsg = "Failed to unmarshall Canonical Action.";
      gatewayEventManager.triggerErrorEvent(this.getClass(), errorMsg, "<UNKNOWN_CASE_ID>", GatewayEventsConfig.FAILED_TO_UNMARSHALL_CANONICAL);
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to convert message into DTO.", e);
    }
    return dto;
  }
}
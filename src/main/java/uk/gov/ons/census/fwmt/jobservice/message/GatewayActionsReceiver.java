package uk.gov.ons.census.fwmt.jobservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.jobservice.service.JobService;

import java.io.IOException;
import java.time.LocalTime;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_CANCEL_RECEIVED;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_CREATE_JOB_RECEIVED;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_UPDATE_RECEIVED;

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
      gatewayEventManager.triggerEvent(String.valueOf(fwmtCreateJobRequest.getCaseId()), CANONICAL_CREATE_JOB_RECEIVED,
          LocalTime.now());
      jobService.createJob(fwmtCreateJobRequest);
      log.info("Sending Create job to TM");
    } else if (message.contains("Cancel")) {
      CancelFieldWorkerJobRequest fwmtCancelJobRequest = convertMessageToDTO(CancelFieldWorkerJobRequest.class, message);
      gatewayEventManager
          .triggerEvent(String.valueOf(fwmtCancelJobRequest.getCaseId()), CANONICAL_CANCEL_RECEIVED, LocalTime.now());
      jobService.cancelJob(fwmtCancelJobRequest);
      log.info("Sending Cancel job to TM");
    } else if (message.contains("Update")) {
      UpdateFieldWorkerJobRequest fwmtUpdateJobRequest = convertMessageToDTO(UpdateFieldWorkerJobRequest.class, message);
      gatewayEventManager
          .triggerEvent(String.valueOf(fwmtUpdateJobRequest.getId()), CANONICAL_UPDATE_RECEIVED, LocalTime.now());
      jobService.updateJob(fwmtUpdateJobRequest);
      log.info("Sending Update job to TM");
    } else {
      throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Cannot process message.");
    }
  }

  private <T> T convertMessageToDTO(Class<T> klass, String message) throws GatewayException {
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    T dto;
    try {
      dto = mapper.readValue(message, klass);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Failed to convert message into DTO.", e);
    }
    return dto;
  }
}
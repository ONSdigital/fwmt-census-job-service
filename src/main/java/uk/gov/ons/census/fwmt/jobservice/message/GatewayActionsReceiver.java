package uk.gov.ons.census.fwmt.jobservice.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  @Autowired
  private ObjectMapper jsonObjectMapper = new ObjectMapper();

  @Autowired
  private MessageConverter messageConverter;

  public void receiveMessage(String message) throws GatewayException {
    log.info("received a message from RM-Adapter");
    convertAndSendMessage(message);
  }

  private void convertAndSendMessage(String actualMessage) throws GatewayException {
    JsonNode actualMessageRootNode;
    try {
      actualMessageRootNode = jsonObjectMapper.readTree(actualMessage);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process message JSON");
    }
    JsonNode gatewayType = actualMessageRootNode.path("gatewayType");
    JsonNode caseId = actualMessageRootNode.path("caseId");

    switch (gatewayType.asText()) {
      case "Create":
        CreateFieldWorkerJobRequest fwmtCreateJobRequest = messageConverter.convertMessageToDTO(CreateFieldWorkerJobRequest.class,
                actualMessage);
        gatewayEventManager.triggerEvent(caseId.asText(), CANONICAL_CREATE_JOB_RECEIVED,
                LocalTime.now());
        jobService.createJob(fwmtCreateJobRequest);
        break;
      case "Cancel":
        CancelFieldWorkerJobRequest fwmtCancelJobRequest = messageConverter.convertMessageToDTO(CancelFieldWorkerJobRequest.class,
                actualMessage);
        gatewayEventManager
                .triggerEvent(caseId.asText(), CANONICAL_CANCEL_RECEIVED, LocalTime.now());
        jobService.cancelJob(fwmtCancelJobRequest);
        break;
      case "Update":
        UpdateFieldWorkerJobRequest fwmtUpdateJobRequest = messageConverter.convertMessageToDTO(UpdateFieldWorkerJobRequest.class,
                actualMessage);
        gatewayEventManager
                .triggerEvent(caseId.asText(), CANONICAL_UPDATE_RECEIVED, LocalTime.now());
        jobService.updateJob(fwmtUpdateJobRequest);
        break;
      default:
        throw new GatewayException(GatewayException.Fault.BAD_REQUEST, "Cannot process message for case ID " + caseId.asText());
    }
    log.info("Sending " + caseId.asText() + " job to TM");
  }
}
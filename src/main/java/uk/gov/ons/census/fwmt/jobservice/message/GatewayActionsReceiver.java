package uk.gov.ons.census.fwmt.jobservice.message;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_CANCEL_RECEIVED;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_CREATE_JOB_RECEIVED;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.CANONICAL_UPDATE_RECEIVED;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
  private ObjectMapper jsonObjectMapper = new ObjectMapper();

  @Autowired
  private MessageConverter messageConverter;

  public void receiveMessage(Object message) throws GatewayException {
    log.info("received a message from RM-Adapter");
    convertAndSendMessage(message);
  }

  private void convertAndSendMessage(Object actualMessage) throws GatewayException {
    String messageToString = actualMessage.toString();
    JsonNode actualMessageRootNode;
    try {
      actualMessageRootNode = jsonObjectMapper.readTree(messageToString);
    } catch (IOException e) {
      throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Cannot process message JSON");
    }
    JsonNode gatewayType = actualMessageRootNode.path("gatewayType");
    JsonNode caseId = actualMessageRootNode.path("caseId");

    switch (gatewayType.asText()) {
      case "Create":
        CreateFieldWorkerJobRequest fwmtCreateJobRequest = messageConverter.convertMessageToDTO(CreateFieldWorkerJobRequest.class,
                messageToString);
        gatewayEventManager.triggerEvent(String.valueOf(fwmtCreateJobRequest.getCaseId()), CANONICAL_CREATE_JOB_RECEIVED);
        jobService.createJob(fwmtCreateJobRequest);
        break;
      case "Cancel":
        CancelFieldWorkerJobRequest fwmtCancelJobRequest = messageConverter.convertMessageToDTO(CancelFieldWorkerJobRequest.class,
                messageToString);
        gatewayEventManager.triggerEvent(String.valueOf(fwmtCancelJobRequest.getCaseId()), CANONICAL_CANCEL_RECEIVED);
        jobService.cancelJob(fwmtCancelJobRequest);
        break;
      case "Update":
        UpdateFieldWorkerJobRequest fwmtUpdateJobRequest = messageConverter.convertMessageToDTO(UpdateFieldWorkerJobRequest.class,
                messageToString);
        gatewayEventManager.triggerEvent(String.valueOf(fwmtUpdateJobRequest.getCaseId()), CANONICAL_UPDATE_RECEIVED);
        jobService.updateJob(fwmtUpdateJobRequest);
        break;
      default:
        String errorMsg = "Invalid Canonical Action.";
        gatewayEventManager.triggerErrorEvent(this.getClass(), errorMsg, "<UNKNOWN_CASE_ID>", GatewayEventsConfig.INVALID_CANONICAL_ACTION);
        throw new GatewayException(GatewayException.Fault.BAD_REQUEST, errorMsg);
    }
    log.info("Sending " + caseId.asText() + " job to TM");
  }
}
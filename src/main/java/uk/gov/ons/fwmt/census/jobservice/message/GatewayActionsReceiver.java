package uk.gov.ons.fwmt.census.jobservice.message;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.service.JobService;

@Slf4j
@Component
public class GatewayActionsReceiver {

  @Autowired
  private JobService jobService;

  @Autowired
  private ObjectMapper mapper;

  public void receiveMessage(String message) throws GatewayException {
    log.info("received a message from RM-Adapter");
    processMessage(message);
  }

  private void processMessage(String message) throws GatewayException {
    if (message.contains("Create")) {
      CreateFieldWorkerJobRequest fwmtCreateJobRequest = convertMessageToDTO(CreateFieldWorkerJobRequest.class, message);
      jobService.createJob(fwmtCreateJobRequest);
      log.info("Sending Create job to TM");
    } else if (message.contains("Cancel")) {
      CancelFieldWorkerJobRequest fwmtCancelJobRequest = convertMessageToDTO(CancelFieldWorkerJobRequest.class, message);
      jobService.cancelJob(fwmtCancelJobRequest);
      log.info("Sending Cancel job to TM");
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
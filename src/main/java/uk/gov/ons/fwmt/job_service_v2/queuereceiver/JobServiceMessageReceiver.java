package uk.gov.ons.fwmt.job_service_v2.queuereceiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service_v2.service.JobService;

import javax.xml.datatype.DatatypeConfigurationException;
import java.io.IOException;

@Slf4j
@Component
public class JobServiceMessageReceiver {

  @Autowired
  private JobService jobService;

  @Autowired
  private ObjectMapper mapper;

  @Retryable(FWMTCommonException.class)
  public void receiveMessage(String message) throws IllegalAccessException, InstantiationException, DatatypeConfigurationException {
    log.info("received a message: " + message);
    processMessage(message);
  }

  @Retryable(FWMTCommonException.class)
  private void processMessage(String message) throws InstantiationException, IllegalAccessException, DatatypeConfigurationException {
    if (message.contains("Create")) {
      FWMTCreateJobRequest fwmtCreateJobRequest = convertMessageToDTO(FWMTCreateJobRequest.class, message);
      jobService.createJob(fwmtCreateJobRequest);
    } else if (message.contains("Cancel")) {
      FWMTCancelJobRequest fwmtCancelJobRequest = convertMessageToDTO(FWMTCancelJobRequest.class, message);
      jobService.cancelJob(fwmtCancelJobRequest);
    } else {
      log.error("Message can be processed due to unknown type");
    }
  }

  @Retryable(FWMTCommonException.class)
  private <T> T convertMessageToDTO(Class<T> klass, String message)
      throws IllegalAccessException, InstantiationException {
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    T dto = klass.newInstance();
    try {
      dto = mapper.readValue(message, klass);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dto;
  }
}
package uk.gov.ons.fwmt.census.jobservice.message.impl;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import uk.gov.ons.fwmt.census.jobservice.config.QueueConfig;
import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.jobservice.message.RMProducer;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

@Slf4j
@Component
public class RMProducerImpl implements RMProducer {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RabbitTemplate template;

  @Retryable
  public void send(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws CTPException {
    try {
      final String notification = objectMapper.writeValueAsString(censusCaseOutcomeDTO);
      log.info("Message sent to queue :{}", censusCaseOutcomeDTO.getCaseReference());
      template.convertAndSend(QueueConfig.GATEWAY_FEEDBACK_EXCHANGE, QueueConfig.JOBSVC_JOB_RESPONSE_ROUTING_KEY, notification);
    } catch (JsonProcessingException e) {
      throw new CTPException(CTPException.Fault.SYSTEM_ERROR, "Failed to process message into JSON.", e);
    }
  }
}

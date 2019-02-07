package uk.gov.ons.fwmt.census.jobservice.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.gov.ons.fwmt.census.jobservice.config.GatewayActionsQueueConfig;
import uk.gov.ons.fwmt.census.jobservice.config.GatewayFeedbackQueueConfig;

@RequestMapping("/rabbitHealth")
public class RabbitCheckHealthController {

  @Autowired
  private ConnectionFactory factory;

  private String checkQueue(RabbitAdmin rabbitAdmin, String queueName) {
    Properties props = rabbitAdmin.getQueueProperties(queueName);
    return (props != null) ? props.getProperty("QUEUE_NAME") : null;
  }

  @RequestMapping(value = "/", method = RequestMethod.GET, produces = "application/json")
  public List<String> rabbitHealth() {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);

    List<String> queues = Arrays.asList(
        GatewayFeedbackQueueConfig.GATEWAY_FEEDBACK_QUEUE, 
        GatewayActionsQueueConfig.GATEWAY_ACTIONS_QUEUE,
        GatewayActionsQueueConfig.GATEWAY_ACTIONS_DLQ
    );

    return queues.stream()
        .map(a -> this.checkQueue(rabbitAdmin, a))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

}

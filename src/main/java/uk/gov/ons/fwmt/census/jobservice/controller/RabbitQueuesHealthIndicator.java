package uk.gov.ons.fwmt.census.jobservice.controller;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;
import uk.gov.ons.fwmt.census.jobservice.config.GatewayActionsQueueConfig;
import uk.gov.ons.fwmt.census.jobservice.config.GatewayFeedbackQueueConfig;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
public class RabbitQueuesHealthIndicator extends AbstractHealthIndicator {

  private static List<String> QUEUES = Arrays.asList(
      GatewayFeedbackQueueConfig.GATEWAY_FEEDBACK_QUEUE,
      GatewayActionsQueueConfig.GATEWAY_ACTIONS_QUEUE,
      GatewayActionsQueueConfig.GATEWAY_ACTIONS_DLQ
  );

  @Autowired
  @Qualifier("connectionFactory")
  private ConnectionFactory connectionFactory;
  private RabbitAdmin rabbitAdmin;

  private String checkQueue(String queueName) {
    Properties props = rabbitAdmin.getQueueProperties(queueName);
    return (props != null) ? props.getProperty("QUEUE_NAME") : null;
  }

  public List<String> getAccessibleQueues() {
    rabbitAdmin = new RabbitAdmin(connectionFactory);

    return QUEUES.stream()
        .map(a -> this.checkQueue(a))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  @Override protected void doHealthCheck(Health.Builder builder) throws Exception {
    List<String> accessibleQueues = getAccessibleQueues();

    builder.withDetail("accessible-queues", accessibleQueues);

    if (accessibleQueues.containsAll(QUEUES)) {
      builder.up();
    } else {
      builder.down();
    }

  }
}

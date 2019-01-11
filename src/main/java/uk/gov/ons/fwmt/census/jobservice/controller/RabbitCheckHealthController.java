package uk.gov.ons.fwmt.census.jobservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class RabbitCheckHealthController {

  @Autowired
  private ConnectionFactory factory;

  private String checkQueue(RabbitAdmin rabbitAdmin, String queueName) {
    Properties props = rabbitAdmin.getQueueProperties(queueName);
    return (props != null) ? props.getProperty("QUEUE_NAME") : null;
  }

  @RequestMapping(value = "/rabbitHealth", method = RequestMethod.GET, produces = "application/json")
  public List<String> rabbitHealth() {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(factory);

    List<String> queues = Arrays.asList(
        "jobsvc-adapter",
        "jobSvc-adapter.DLQ",
        "adapter-jobSvc",
        "adapter-jobSvc.DLQ",
        "adapter-rm",
        "adapter-rm.DLQ",
        "rm-adapter.DLQ"
    );

    return queues.stream()
        .map(a -> this.checkQueue(rabbitAdmin, a))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

}

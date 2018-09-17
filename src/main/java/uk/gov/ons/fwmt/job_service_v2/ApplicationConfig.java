/*
 * Copyright.. etc
 */

package uk.gov.ons.fwmt.job_service_v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueConfig;
import uk.gov.ons.fwmt.fwmtgatewaycommon.exceptions.types.FWMTCommonException;
import uk.gov.ons.fwmt.job_service_v2.queuereceiver.JobServiceMessageReceiver;

import java.util.HashMap;
import java.util.Map;

/**
 * Main entry point into the TM Gateway
 *
 * @author Chris Hardman
 */

@Slf4j
@SpringBootApplication
@Configuration
@EnableRetry
public class ApplicationConfig {

  public static void main(String[] args) {
    SpringApplication.run(ApplicationConfig.class, args);
    log.debug("Started application");
  }

  @Bean
  Queue adapterQueue() {
    return new Queue(QueueConfig.JOBSVC_TO_ADAPTER_QUEUE, false);
  }

  @Bean
  Queue jobsvcQueue() {
    return new Queue(QueueConfig.ADAPTER_TO_JOBSVC_QUEUE, false);
  }

  @Bean
  TopicExchange adapterExchange() {
    return new TopicExchange(QueueConfig.RM_JOB_SVC_EXCHANGE);
  }

  @Bean
  Binding adapterBinding(@Qualifier("adapterQueue") Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(QueueConfig.JOB_SVC_RESPONSE_ROUTING_KEY);
  }

  @Bean
  Binding jobsvcBinding(@Qualifier("jobsvcQueue") Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(QueueConfig.JOB_SVC_REQUEST_ROUTING_KEY);
  }

  @Bean
  SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(QueueConfig.ADAPTER_TO_JOBSVC_QUEUE);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter listenerAdapter(JobServiceMessageReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean
  RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    Map<Class<? extends Throwable>, Boolean> includeExceptions = new HashMap<>();
    includeExceptions.put(FWMTCommonException.class, true);
    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(5000);
    backOffPolicy.setMultiplier(3.0);
    backOffPolicy.setMaxInterval(45000);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3, includeExceptions);
    retryPolicy.setMaxAttempts(3);
    retryTemplate.setRetryPolicy(retryPolicy);

    return retryTemplate;
  }
}

package uk.gov.ons.census.fwmt.jobservice.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import uk.gov.ons.census.fwmt.jobservice.message.GatewayActionsReceiver;

@Configuration
public class GatewayActionsQueueConfig {
  public static final String GATEWAY_ACTIONS_QUEUE = "Gateway.Actions";
  public static final String GATEWAY_ACTIONS_DLQ = "Gateway.ActionsDLQ";
  public static final String GATEWAY_ACTIONS_ROUTING_KEY = "Gateway.Action.Request";

  private int concurrentConsumers;

  public GatewayActionsQueueConfig(@Value("${rabbitmq.concurrentConsumers}") Integer concurrentConsumers) {
    this.concurrentConsumers = concurrentConsumers;
  }

  //Queues
  @Bean
  public Queue gatewayActionsQueue() {
    return QueueBuilder.durable(GATEWAY_ACTIONS_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", GATEWAY_ACTIONS_DLQ)
        .build();
  }

  //Dead Letter Queue
  @Bean
  public Queue gatewayActionsDeadLetterQueue() {
    return QueueBuilder.durable(GATEWAY_ACTIONS_DLQ).build();
  }

  //Listener Adapter
  @Bean
  public MessageListenerAdapter gatewayActionsListenerAdapter(GatewayActionsReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  //Message Listener
  @Bean
  public SimpleMessageListenerContainer gatewayActionsMessageListener(
      @Qualifier("connectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("gatewayActionsListenerAdapter") MessageListenerAdapter messageListenerAdapter,
      @Qualifier("interceptor") RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    Advice[] adviceChain = {retryOperationsInterceptor};
    messageListenerAdapter.setMessageConverter(new Jackson2JsonMessageConverter());
    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(GATEWAY_ACTIONS_QUEUE);
    container.setMessageListener(messageListenerAdapter);
    container.setConcurrentConsumers(concurrentConsumers);
    return container;
  }

}

package uk.gov.ons.fwmt.census.jobservice.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import uk.gov.ons.fwmt.census.jobservice.message.GatewayFeedbackProducer;

@Configuration
public class GatewayActionsQueueConfig {
  @Autowired
  private AmqpAdmin amqpAdmin;
  
  public static final String GATEWAY_ACTIONS_QUEUE = "gateway.actions";
  public static final String GATEWAY_ACTIONS_DLQ = "gateway.actions.DLQ";

  //Queues
  @Bean
  public Queue gatewayActionsQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_ACTIONS_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", GATEWAY_ACTIONS_DLQ)
        .build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  } 

  //Dead Letter Queue
  @Bean
  public Queue gatewayActionsDeadLetterQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_ACTIONS_DLQ).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  }

  //Listener Adapter
  @Bean
  public MessageListenerAdapter gatewayActionsListenerAdapter(GatewayFeedbackProducer receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }
  
  //Message Listener
  @Bean
  public SimpleMessageListenerContainer gatewayActionsMessagerListener(
      @Qualifier("connectionFactory") ConnectionFactory connectionFactory,
      @Qualifier("gatewayActionsListenerAdapter") MessageListenerAdapter messageListenerAdapter,
      @Qualifier("interceptor") RetryOperationsInterceptor retryOperationsInterceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    Advice[] adviceChain = {retryOperationsInterceptor};
    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(GATEWAY_ACTIONS_QUEUE);
    container.setMessageListener(messageListenerAdapter);
    return container;
  }

}
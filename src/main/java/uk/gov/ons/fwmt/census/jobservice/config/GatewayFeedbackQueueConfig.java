package uk.gov.ons.fwmt.census.jobservice.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayFeedbackQueueConfig {
  @Autowired
  private AmqpAdmin amqpAdmin;
    
  public static final String GATEWAY_FEEDBACK_QUEUE = "Gateway.Feedback";
  public static final String GATEWAY_FEEDBACK_EXCHANGE = "Gateway.Feedback.Exchange";
  public static final String GATEWAY_FEEDBACK_ROUTING_KEY = "Gateway.Feedback.Request";

  // Queue
  @Bean
  public Queue gatewayFeedbackQueue() {
    Queue queue = QueueBuilder.durable(GATEWAY_FEEDBACK_QUEUE).build();
    queue.setAdminsThatShouldDeclare(amqpAdmin);
    return queue;
  }
  
  //Exchange
  @Bean
  public DirectExchange gatewayFeedbackExchange() {
    DirectExchange directExchange = new DirectExchange(GATEWAY_FEEDBACK_EXCHANGE);
    directExchange.setAdminsThatShouldDeclare(amqpAdmin);
    return directExchange;
  }

  // Bindings
  @Bean
  public Binding gatewayFeedbackBinding(@Qualifier("gatewayFeedbackQueue") Queue gatewayFeedbackQueue,
      @Qualifier("gatewayFeedbackExchange") DirectExchange gatewayFeedbackExchange) {
    Binding binding = BindingBuilder.bind(gatewayFeedbackQueue).to(gatewayFeedbackExchange)
        .with(GATEWAY_FEEDBACK_ROUTING_KEY);
    binding.setAdminsThatShouldDeclare(amqpAdmin);
    return binding;
  }
}

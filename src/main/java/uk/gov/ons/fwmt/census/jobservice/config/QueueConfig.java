package uk.gov.ons.fwmt.census.jobservice.config;

import org.aopalliance.aop.Advice;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryOperations;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.support.RetryTemplate;
import uk.gov.ons.fwmt.census.jobservice.message.JobServiceMessageReceiver;
import uk.gov.ons.fwmt.census.jobservice.retrysupport.DefaultListenerSupport;
import uk.gov.ons.fwmt.fwmtgatewaycommon.retry.CTPRetryPolicy;
import uk.gov.ons.fwmt.fwmtgatewaycommon.retry.CustomMessageRecover;

@Configuration
public class QueueConfig {

  private final int initialInterval;
  private final double multiplier;
  private final int maxInterval;

  public static final String GATEWAY_FEEDBACK = "gateway.feedback";
  public static final String GATEWAY_ACTIONS = "gateway.actions";
  public static final String GATEWAY_FEEDBACK_DLQ = "gateway.feedback.DLQ";
  public static final String GATEWAY_ACTIONS_DLQ = "gateway.actions.DLQ";
  public static final String GATEWAY_FEEDBACK_EXCHANGE = "gateway.feedback.exchange";
  public static final String JOBSVC_JOB_RESPONSE_ROUTING_KEY = "jobsvc.job.response";
  public static final String JOBSVC_REQUEST_ROUTING_KEY = "jobsvc.job.request";

  public QueueConfig(@Value("${rabbitmq.initialinterval}") Integer initialInterval,
      @Value("${rabbitmq.multiplier}") Double multiplier,
      @Value("${rabbitmq.maxInterval}") Integer maxInterval) {
    this.initialInterval = initialInterval;
    this.multiplier = multiplier;
    this.maxInterval = maxInterval;
  }

  @Bean
  public Queue gatewayFeedbackQueue() {
    return QueueBuilder.durable(GATEWAY_FEEDBACK)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", GATEWAY_FEEDBACK_DLQ)
        .build();
  }

  @Bean
  public Queue gatewayActionsQueue() {
    return QueueBuilder.durable(GATEWAY_ACTIONS)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", GATEWAY_ACTIONS_DLQ)
        .build();
  }

  @Bean
  public Queue gatewayActionsDeadLetterQueue() {
    return QueueBuilder.durable(GATEWAY_ACTIONS_DLQ).build();
  }

  @Bean
  public Queue gatewayFeedbackDeadLetterQueue() {
    return QueueBuilder.durable(GATEWAY_FEEDBACK_DLQ).build();
  }

  @Bean
  public DirectExchange gatewayFeedbackExchange() {
    return new DirectExchange(GATEWAY_FEEDBACK_EXCHANGE);
  }

  @Bean
  public Binding gatewayFeedbackBinding(@Qualifier("gatewayFeedbackQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(JOBSVC_JOB_RESPONSE_ROUTING_KEY);
  }

  @Bean
  public Binding gatewayActionsBinding(@Qualifier("gatewayActionsQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(JOBSVC_REQUEST_ROUTING_KEY);
  }

  @Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter, RetryOperationsInterceptor interceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

    Advice[] adviceChain = {interceptor};

    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(GATEWAY_ACTIONS);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  public MessageListenerAdapter listenerAdapter(JobServiceMessageReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean
  public RetryOperationsInterceptor interceptor(RetryOperations retryTemplate) {
    RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
    interceptor.setRecoverer(new CustomMessageRecover());
    interceptor.setRetryOperations(retryTemplate);
    return interceptor;
  }

  @Bean
  public RetryTemplate retryTemplate() {
    RetryTemplate retryTemplate = new RetryTemplate();

    ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(initialInterval);
    backOffPolicy.setMultiplier(multiplier);
    backOffPolicy.setMaxInterval(maxInterval);
    retryTemplate.setBackOffPolicy(backOffPolicy);

    CTPRetryPolicy ctpRetryPolicy = new CTPRetryPolicy();
    retryTemplate.setRetryPolicy(ctpRetryPolicy);

    retryTemplate.registerListener(new DefaultListenerSupport());

    return retryTemplate;
  }
}

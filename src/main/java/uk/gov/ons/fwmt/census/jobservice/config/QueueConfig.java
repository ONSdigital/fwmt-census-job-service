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

  private static final String JOBSVC_TO_ADAPTER_QUEUE = "gateway.feedback";
  private static final String ADAPTER_TO_JOBSVC_QUEUE = "gateway.actions";
  private static final String JOBSVC_TO_ADAPTER_DLQ = JOBSVC_TO_ADAPTER_QUEUE + ".DLQ";
  private static final String ADAPTER_TO_JOBSVC_DLQ = ADAPTER_TO_JOBSVC_QUEUE + ".DLQ";
  private static final String RM_JOB_SVC_EXCHANGE = "gateway.feedback.exchange";
  private static final String JOB_SVC_RESPONSE_ROUTING_KEY = "jobsvc.job.response";
  private static final String JOB_SVC_REQUEST_ROUTING_KEY = "jobsvc.job.request";

  public QueueConfig(@Value("${rabbitmq.initialinterval}") Integer initialInterval,
      @Value("${rabbitmq.multiplier}") Double multiplier,
      @Value("${rabbitmq.maxInterval}") Integer maxInterval) {
    this.initialInterval = initialInterval;
    this.multiplier = multiplier;
    this.maxInterval = maxInterval;
  }

  @Bean
  public Queue adapterQueue() {
    return QueueBuilder.durable(JOBSVC_TO_ADAPTER_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", JOBSVC_TO_ADAPTER_DLQ)
        .build();
  }

  @Bean
  public Queue jobSvcQueue() {
    return QueueBuilder.durable(ADAPTER_TO_JOBSVC_QUEUE)
        .withArgument("x-dead-letter-exchange", "")
        .withArgument("x-dead-letter-routing-key", ADAPTER_TO_JOBSVC_DLQ)
        .build();
  }

  @Bean
  public Queue adapterDeadLetterQueue() {
    return QueueBuilder.durable(ADAPTER_TO_JOBSVC_DLQ).build();
  }

  @Bean
  public Queue jobSvsDeadLetterQueue() {
    return QueueBuilder.durable(JOBSVC_TO_ADAPTER_DLQ).build();
  }

  @Bean
  public DirectExchange adapterExchange() {
    return new DirectExchange(RM_JOB_SVC_EXCHANGE);
  }

  @Bean
  public Binding adapterBinding(@Qualifier("adapterQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(JOB_SVC_RESPONSE_ROUTING_KEY);
  }

  @Bean
  public Binding jobSvcBinding(@Qualifier("jobSvcQueue") Queue queue, DirectExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(JOB_SVC_REQUEST_ROUTING_KEY);
  }

  @Bean
  public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
      MessageListenerAdapter listenerAdapter, RetryOperationsInterceptor interceptor) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

    Advice[] adviceChain = {interceptor};

    container.setAdviceChain(adviceChain);
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(ADAPTER_TO_JOBSVC_QUEUE);
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

package uk.gov.ons.fwmt.census.jobservice;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import uk.gov.ons.fwmt.census.jobservice.config.GatewayFeedbackQueueConfig;
import uk.gov.ons.fwmt.census.jobservice.config.QueueConfig;
import uk.gov.ons.fwmt.census.jobservice.helper.TestReceiver;


@Configuration
public class IntegrationTestConfig {

  @Value("${security.user.name}")
  String userName;
  @Value("${security.user.password}")
  String password;

  @Bean
  SimpleMessageListenerContainer testRequestContainer(ConnectionFactory connectionFactory,
      @Qualifier("testListenerAdapter") MessageListenerAdapter listenerAdapter) {
    SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
    container.setConnectionFactory(connectionFactory);
    container.setQueueNames(GatewayFeedbackQueueConfig.GATEWAY_FEEDBACK_QUEUE);
    container.setMessageListener(listenerAdapter);
    return container;
  }

  @Bean
  MessageListenerAdapter testListenerAdapter(TestReceiver receiver) {
    return new MessageListenerAdapter(receiver, "receiveMessage");
  }

  @Bean("testWSTemplate")
  public WebServiceTemplate webServiceTemplate() throws Exception {
    WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setContextPath("com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging");
    webServiceTemplate.setMarshaller(marshaller);
    webServiceTemplate.setUnmarshaller(marshaller);
    HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
    messageSender.setCredentials(new UsernamePasswordCredentials("user", "password"));
    messageSender.afterPropertiesSet();
    webServiceTemplate.setMessageSender(messageSender);
    return webServiceTemplate;
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return mapper;
  }
}

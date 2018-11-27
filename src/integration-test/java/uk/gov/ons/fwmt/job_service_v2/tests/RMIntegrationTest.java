package uk.gov.ons.fwmt.job_service_v2.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.fwmtgatewaycommon.config.QueueNames;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.MockMessage;
import uk.gov.ons.fwmt.job_service_v2.IntegrationTestConfig;
import uk.gov.ons.fwmt.job_service_v2.helper.TestReceiver;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Component
@Slf4j
@ActiveProfiles("integration")
@Import({IntegrationTestConfig.class, TestReceiver.class})
public class RMIntegrationTest {

  @Value("${server.port}")
  private int port;

  @Value("${mock.port}")
  private int mockPort;

  private String mockUrl;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private RestTemplate restTemplate = new RestTemplate();

  @PostConstruct
  public void postConstruct() {
    mockUrl = "http://localhost:" + mockPort;
  }

  @BeforeAll
  public void testSetup() {
    log.debug("Mock port: " + mockPort);
    log.debug("Mock url: " + mockUrl);
    restTemplate.getForObject(mockUrl + "/logger/reset", Void.class);
  }

  @Test
  @Disabled(" Broken test ")
  // TODO rewrite based on changes to queues coming from RM
  public void receiveRMCreateMessage_checkTMReceivedMessage() throws InterruptedException, JsonProcessingException {
    sendCreateMessage();
    Thread.sleep(7000);
    MockMessage[] messages = restTemplate.getForObject(mockUrl + "/logger/allMessages", MockMessage[].class);
    assertNotNull(messages);
    assertEquals(1, messages.length);
  }

  @Test
  @Disabled(" Broken test ")
  // TODO rewrite based on changes to queues going back to RM
  public void receiveRMCancelMessage_checkTMReceivedMessage() throws InterruptedException, JsonProcessingException {
    MockMessage[] initialMessages = restTemplate.getForObject(mockUrl + "/logger/allMessages", MockMessage[].class);
    assertNotNull(initialMessages);
    int initialCount = initialMessages.length;
    sendCancelMessage();
    Thread.sleep(7000);
    MockMessage[] finalMessages = restTemplate.getForObject(mockUrl + "/logger/allMessages", MockMessage[].class);
    assertNotNull(finalMessages);
    int finalCount = finalMessages.length;
    assertEquals(1, (finalCount - initialCount));
  }

  private void sendCreateMessage() throws JsonProcessingException {
    FWMTCreateJobRequest fwmtCreateJobRequest = new FWMTCreateJobRequest();
    Address address = new Address();
    address.setLatitude(BigDecimal.valueOf(61.7921776));
    address.setLongitude(BigDecimal.valueOf(34.3739957));
    address.setLine1("886");
    address.setLine2("Prairie Rose");
    address.setLine3("Trail");
    address.setLine4("RU");
    address.setPostCode("188961");
    address.setTownName("Borodinskiy");

    fwmtCreateJobRequest.setJobIdentity("1234");
    fwmtCreateJobRequest.setSurveyType("HH");
    fwmtCreateJobRequest.setDueDate(LocalDate.parse("20180216", DateTimeFormatter.BASIC_ISO_DATE));
    fwmtCreateJobRequest.setAddress(address);
    fwmtCreateJobRequest.setActionType("Create");

    String JSONJobRequest = null;

    JSONJobRequest = objectMapper.writeValueAsString(fwmtCreateJobRequest);

    log.info("Message send to queue"+ JSONJobRequest);
    rabbitTemplate.convertAndSend(QueueNames.RM_JOB_SVC_EXCHANGE, QueueNames.JOB_SVC_REQUEST_ROUTING_KEY, JSONJobRequest);
  }

  private void sendCancelMessage() throws JsonProcessingException {
    FWMTCancelJobRequest fwmtCancelJobRequest = new FWMTCancelJobRequest();
    fwmtCancelJobRequest.setActionType("Cancel");
    fwmtCancelJobRequest.setJobIdentity("1234");
    fwmtCancelJobRequest.setReason("wrong address");

    String JSONJobRequest = null;
    JSONJobRequest = objectMapper.writeValueAsString(fwmtCancelJobRequest);

    rabbitTemplate.convertAndSend(QueueNames.RM_JOB_SVC_EXCHANGE, QueueNames.JOB_SVC_REQUEST_ROUTING_KEY, JSONJobRequest);

  }
}

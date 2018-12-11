package uk.gov.ons.fwmt.census.jobservice.tm.client;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.DeleteMessageRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.ObjectFactory;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.QueryMessagesRequest;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.QueryMessagesResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendAddJobTasksRequestMessage;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessageResponse;
import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendMessageRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ws.client.core.WebServiceTemplate;
import uk.gov.ons.fwmt.census.jobservice.converter.TMConverter;
import uk.gov.ons.fwmt.census.jobservice.converter.impl.HouseholdConverter;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.Address;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

import javax.xml.bind.JAXBElement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class TMJobServiceImplTest {
  // currently, this ExpectedException is not used, and thus throws linting errors
  //  @Rule
  //  public ExpectedException expectedException = ExpectedException.none();

  @InjectMocks
  private TMClient tmServiceImpl;

  @Mock
  private ObjectFactory objectFactory;

  @Mock
  private WebServiceTemplate webServiceTemplate;

  @Mock
  private JAXBElement<Object> jaxbElement;

  @Mock
  private Map<String, TMConverter> tmConverters;

  @Before
  public void setUp() throws Exception {
    tmServiceImpl = new TMClient("https://ons.totalmobile.co.uk",
        "messageQueuePath",
        "messageQueuePackage",
        "expectedNamespace",
        "username", "password");
    MockitoAnnotations.initMocks(this);
  }

  @Test(expected = IllegalArgumentException.class)
  public void soapLookupReceivesAMessageThatDoesNotMatchTM() {
    //When
    tmServiceImpl.lookupSOAPAction(Object.class);
  }

  @Test
  public void soapLookupReceivesARequestMessage() {
    //Given
    String expectedResult = "expectedNamespaceQuery";

    //When
    String result = tmServiceImpl.lookupSOAPAction(QueryMessagesRequest.class);

    //Then
    assertEquals(expectedResult, result);
  }

  @Test
  public void soapLookupReceivesNonMappedRequestMessage() {
    //Given
    String expectedResult = "expectedNamespaceSendAddJobTasksRequestMessage";

    //When
    String result = tmServiceImpl.lookupSOAPAction(SendAddJobTasksRequestMessage.class);

    //Then
    assertEquals(expectedResult, result);
  }

  @Test
  public void shouldTestIfCreateSendMessageRequestIsCalled() {
    //Given
    SendMessageRequest sendMessageRequest = new SendMessageRequest();

    //When
    tmServiceImpl.jaxbWrap(sendMessageRequest);

    //Then
    Mockito.verify(objectFactory).createSendMessageRequest(sendMessageRequest);
  }

  @Test
  public void shouldTestIfCreateQueryMessageRequestIsCalled() {
    //Given
    QueryMessagesRequest queryMessagesRequest = new QueryMessagesRequest();

    //When
    tmServiceImpl.jaxbWrap(queryMessagesRequest);

    //Then
    Mockito.verify(objectFactory).createQueryMessagesRequest(any());
  }

  @Test
  public void shouldHandleAnyOtherObject() {
    //Given
    DeleteMessageRequest deleteMessageRequest = new DeleteMessageRequest();

    //When
    Object result = tmServiceImpl.jaxbWrap(deleteMessageRequest);

    //Then
    Assert.assertTrue(result instanceof DeleteMessageRequest);
  }

  @Test
  public void sendsAResponseToBeUnwrapped() {
    //Given
    SendCreateJobRequestMessageResponse response = new SendCreateJobRequestMessageResponse();

    //When
    Object result = tmServiceImpl.jaxbUnwrap(response);

    //Then
    assertEquals(response, result);
  }

  @Test
  public void sendsAJAXBElementToBeUnwrapped() {
    //Given
    SendCreateJobRequestMessageResponse response = new SendCreateJobRequestMessageResponse();
    when(jaxbElement.getValue()).thenReturn(response);

    //When
    Object result = tmServiceImpl.jaxbUnwrap(jaxbElement);

    //Then
    assertEquals(response, result);
  }

  @Test
  public void shouldReturnSuccessfulResponseWhenReceivesPermittedResponse() {
    //Given
    QueryMessagesRequest queryMessagesRequest = new QueryMessagesRequest();
    when(webServiceTemplate.marshalSendAndReceive(any(), any(), any())).thenReturn(jaxbElement);
    QueryMessagesResponse queryMessagesResponse = new QueryMessagesResponse();
    when(jaxbElement.getValue()).thenReturn(queryMessagesResponse);

    //When
    QueryMessagesResponse result = tmServiceImpl.send(queryMessagesRequest);

    //Then
    assertEquals(queryMessagesResponse, result);
  }

  @Test
  public void shouldThrowExceptionWhenReceivedResponseWIsNotInPermittedResponses() {
    //Given
    QueryMessagesRequest queryMessagesRequest = new QueryMessagesRequest();
    when(webServiceTemplate.marshalSendAndReceive(any(), any(), any())).thenReturn(jaxbElement);
    when(jaxbElement.getValue()).thenReturn(new Object());

    /*
     * Add common exception to common gateway
     * */
    //    expectedException.expect(FWMTCommonException.class);
    //    expectedException.expectMessage(ExceptionCode.TM_MALFUNCTION.getCode());

    //When
    tmServiceImpl.send(queryMessagesRequest);
  }

  @Test
  public void createJob() throws CTPException {
    FWMTCreateJobRequest fwmtCreateJobRequest = new FWMTCreateJobRequest();
    Address address = new Address();
    address.setPostCode("188961");
    address.setTownName("Borodinskiy");

    fwmtCreateJobRequest.setJobIdentity("1234");
    fwmtCreateJobRequest.setSurveyType("HH");
    fwmtCreateJobRequest.setDueDate(LocalDate.parse("20180216", DateTimeFormatter.BASIC_ISO_DATE));
    fwmtCreateJobRequest.setAddress(address);
    fwmtCreateJobRequest.setActionType("Create");

    when(tmConverters.get(any())).thenReturn((TMConverter) new HouseholdConverter());
    when(webServiceTemplate.marshalSendAndReceive(any(), any(), any())).thenReturn(jaxbElement);
    when(jaxbElement.getValue()).thenReturn(new Object());

    tmServiceImpl.createJob(fwmtCreateJobRequest);
  }

}
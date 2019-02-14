package uk.gov.ons.fwmt.census.jobservice.queuereceiver;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;

import java.io.IOException;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.events.component.GatewayEventManager;
import uk.gov.ons.fwmt.census.jobservice.message.GatewayActionsReceiver;
import uk.gov.ons.fwmt.census.jobservice.service.JobService;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceMessageReceiverTest {

  @InjectMocks
  GatewayActionsReceiver messageReceiver;

  @Mock
  private JobService jobService;

  @Mock
  private ObjectMapper mapper;
  
  @Mock
  private GatewayEventManager gatewayEventManager;

  @Test
  public void receiveMessageCreate()
      throws GatewayException, JsonParseException, JsonMappingException, IOException {
    JSONObject json = new JSONObject();
    JSONObject address = new JSONObject();
    json.put("actionType", "Create");
    json.put("jobIdentity", "1234");
    json.put("surveyType", "HH");
    json.put("preallocatedJob", "true");
    json.put("mandatoryResourceAuthNo", "1234");
    json.put("dueDate", "20180216");
    address.put("line1", "886");
    address.put("line2", "Prairie Rose");
    address.put("line3", "Trail");
    address.put("line4", "RU");
    address.put("townName", "Borodinskiy");
    address.put("postCode", "188961");
    address.put("latitude", "61.7921776");
    address.put("longitude", "34.3739957");
    json.put("address", address);
    
    CreateFieldWorkerJobRequest request = new CreateFieldWorkerJobRequest(); 
    request.setJobIdentity("1234");
    Mockito.when(mapper.readValue(anyString(), eq(CreateFieldWorkerJobRequest.class))).thenReturn(request);

    
    String message = json.toString();
    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService).createJob(any());
    Mockito.verify(jobService, never()).cancelJob(any());
  }

  @Test
  public void receiveMessageCancel()
      throws GatewayException, JsonParseException, JsonMappingException, IOException {
    JSONObject json = new JSONObject();
    json.put("actionType", "Cancel");
    json.put("jobIdentity", "1234");
    json.put("reason", "incorrect address");

    CancelFieldWorkerJobRequest request = new CancelFieldWorkerJobRequest(); 
    request.setJobIdentity("1234");
    Mockito.when(mapper.readValue(anyString(), eq(CancelFieldWorkerJobRequest.class))).thenReturn(request);

    
    String message;
    message = json.toString();

    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService, never()).createJob(any());
    Mockito.verify(jobService).cancelJob(any());
  }

}

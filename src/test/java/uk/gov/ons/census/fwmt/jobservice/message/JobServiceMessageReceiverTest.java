package uk.gov.ons.census.fwmt.jobservice.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.jobservice.service.JobService;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;

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
  public void receiveMessageCreate() throws GatewayException, IOException {
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
    Mockito.when(mapper.readValue(anyString(), eq(CreateFieldWorkerJobRequest.class))).thenReturn(request);

    String message = json.toString();
    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService).createJob(any());
    Mockito.verify(jobService, never()).cancelJob(any());
  }

  @Test
  public void receiveMessageCancel() throws GatewayException, IOException {
    JSONObject json = new JSONObject();
    json.put("actionType", "Cancel");
    json.put("caseId", "8ed3fc08-e95f-44db-a6d7-cde4e76a6182");
    json.put("reason", "incorrect address");

    CancelFieldWorkerJobRequest request = new CancelFieldWorkerJobRequest();
    request.setCaseId(UUID.fromString("8ed3fc08-e95f-44db-a6d7-cde4e76a6182"));
    Mockito.when(mapper.readValue(anyString(), eq(CancelFieldWorkerJobRequest.class))).thenReturn(request);

    String message = json.toString();

    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService, never()).createJob(any());
    Mockito.verify(jobService).cancelJob(any());
  }

  @Test
  public void receiveNisraMessageCreate() throws GatewayException, IOException {
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
    json.put("mandatoryResource", "testFieldOfficerId");

    CreateFieldWorkerJobRequest request = new CreateFieldWorkerJobRequest();
    Mockito.when(mapper.readValue(anyString(), eq(CreateFieldWorkerJobRequest.class))).thenReturn(request);

    String message = json.toString();
    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService).createJob(any());
    Mockito.verify(jobService, never()).cancelJob(any());
  }

  @Test(expected = GatewayException.class)
  public void receiveBadProcessMessage() throws GatewayException {
    JSONObject json = new JSONObject();
    json.put("actionType", "");
    String message = json.toString();

    messageReceiver.receiveMessage(message);
  }
  
  @Test
  public void receiveMessageUpdate() throws GatewayException, IOException {
    JSONObject json = new JSONObject();
    json.put("actionType", "update");
    json.put("caseId", "f98e469e-1727-4ef8-bc87-354a7ebdf1de");

    UpdateFieldWorkerJobRequest request = new UpdateFieldWorkerJobRequest();
    request.setCaseId(UUID.fromString("f98e469e-1727-4ef8-bc87-354a7ebdf1de"));
    Mockito.when(mapper.readValue(anyString(), eq(UpdateFieldWorkerJobRequest.class))).thenReturn(request);

    String message = json.toString();

    messageReceiver.receiveMessage(message);

    Mockito.verify(jobService).updateJob(any());
  }
}

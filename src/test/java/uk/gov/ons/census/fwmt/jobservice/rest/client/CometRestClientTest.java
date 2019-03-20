package uk.gov.ons.census.fwmt.jobservice.rest.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Contact;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CometRestClientTest {

  @Mock
  private RestTemplate restTemplate;

  @Test
  public void sendCreateJobRequestTest() {

    CometRestClient cometRestClient = new CometRestClient(restTemplate, "bla/", "test/");
    CaseRequest caseRequest = new CaseRequest();
    caseRequest.setAddress(new Address());
    caseRequest.setContact(new Contact());
    caseRequest.setReference("qwertyu");
    HttpEntity<CaseRequest> body = new HttpEntity<>(caseRequest);

    cometRestClient.sendCreateJobRequest(caseRequest, "2ff1b10e-f841-43d4-a129-7269ac239b61");

    verify(restTemplate).exchange("bla/test/2ff1b10e-f841-43d4-a129-7269ac239b61", HttpMethod.PUT, body, Void.class);
  }
}

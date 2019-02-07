package uk.gov.ons.fwmt.census.jobservice.rest.client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.Address;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.Contact;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CometRestClientTest {

  @Mock
  private RestTemplate restTemplate;

  @Test
  public void sendCreateJobRequestTest() {

    CometRestClient cometRestClient = new CometRestClient(restTemplate, "bla/", "test/");
    ModelCase modelCase = new ModelCase();
    modelCase.setId(UUID.fromString("2ff1b10e-f841-43d4-a129-7269ac239b61"));
    modelCase.setAddress(new Address());
    modelCase.setContact(new Contact());
    modelCase.setReference("qwertyu");
    HttpEntity<ModelCase> body = new HttpEntity<>(modelCase);

    cometRestClient.sendCreateJobRequest(modelCase);

    verify(restTemplate).exchange("bla/test/2ff1b10e-f841-43d4-a129-7269ac239b61", HttpMethod.PUT, body, Void.class);
  }
}

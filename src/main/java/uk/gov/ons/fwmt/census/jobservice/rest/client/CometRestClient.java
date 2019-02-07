package uk.gov.ons.fwmt.census.jobservice.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.utils.JobServiceUtils;

@Component
public class CometRestClient {

  private transient RestTemplate restTemplate;

  private transient String cometURL;

  @Autowired
  public CometRestClient(
      RestTemplate restTemplate,
      @Value("${totalmobile.baseUrl}") String baseUrl,
      @Value("${totalmobile.operation.case.create.path}") String tmPath) {
    this.restTemplate = restTemplate;
    this.cometURL = baseUrl + tmPath;
  }

  public void sendCreateJobRequest(ModelCase modelCase) {
      JobServiceUtils.printJSON(modelCase);
      HttpEntity<ModelCase> body = new HttpEntity<>(modelCase);
      restTemplate.exchange(cometURL + modelCase.getId(), HttpMethod.PUT, body, Void.class);
  }
}

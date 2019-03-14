package uk.gov.ons.census.fwmt.jobservice.rest.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.jobservice.utils.JobServiceUtils;

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

  public void sendCreateJobRequest(CaseRequest caseRequest, String caseId) {
    JobServiceUtils.printJSON(caseRequest);
    HttpEntity<CaseRequest> body = new HttpEntity<>(caseRequest);
    restTemplate.exchange(cometURL + caseId, HttpMethod.PUT, body, Void.class);
  }
}

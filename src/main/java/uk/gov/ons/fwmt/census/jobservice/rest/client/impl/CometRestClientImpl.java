package uk.gov.ons.fwmt.census.jobservice.rest.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.rest.client.CometRestClient;
import uk.gov.ons.fwmt.census.jobservice.utils.JobServiceUtils;

@Slf4j
@Component
public class CometRestClientImpl implements CometRestClient {

  private transient RestTemplate restTemplate;

  private transient String cometURL;

  @Autowired
  public CometRestClientImpl(
      RestTemplate restTemplate,
      @Value("${service.mock.baseUrl}") String baseUrl,
      @Value("${service.mock.operation.case.create.path}") String mockPath) {
    this.restTemplate = restTemplate;
    this.cometURL = baseUrl + mockPath;
  }

  /// Throws an HttpClientErrorException if an error occurs during sending
  public void sendCreateJobRequest(ModelCase modelCase) {
//    try {
      JobServiceUtils.printJSON(modelCase);
      HttpEntity<ModelCase> body = new HttpEntity<>(modelCase);
      restTemplate.exchange(cometURL + modelCase.getId(), HttpMethod.PUT, body, Void.class);
//    } catch (HttpClientErrorException httpClientErrorException) {
//      log.error("An error occurred while sending file to Job Service", httpClientErrorException);
//      throw httpClientErrorException;
//    }
  }
}

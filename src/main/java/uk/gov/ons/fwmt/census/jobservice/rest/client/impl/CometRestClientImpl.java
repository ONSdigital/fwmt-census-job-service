package uk.gov.ons.fwmt.census.jobservice.rest.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.census.jobservice.rest.client.CometRestClient;

@Slf4j
@Component
public class CometRestClientImpl implements CometRestClient {

  private transient RestTemplate restTemplate;

  private transient String cometURL;

  @Autowired
  public CometRestClientImpl(
      RestTemplate restTemplate,
      @Value("${service.job.baseUrl}") String baseUrl,
      @Value("${service.job.operation.jobs.create.path}") String jobServicePath) {
    this.restTemplate = restTemplate;
    this.cometURL = baseUrl + jobServicePath;
  }

  @Override
  public void sendCreateJobRequest() {
    try {

      //      restTemplate.exchange(cometURL, HttpMethod.POST, request, String.class);
    } catch (org.springframework.web.client.HttpClientErrorException HttpClientErrorException) {
      log.error("An error occurred while sending file to Job Service", HttpClientErrorException);
      throw new RuntimeException(HttpClientErrorException);
    }
  }
}

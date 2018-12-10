package uk.gov.ons.fwmt.census.jobservice.rest.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.census.jobservice.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.rest.client.CometRestClient;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

import java.util.Map;

@Slf4j
@Component
public class CometRestClientImpl implements CometRestClient {

  private transient RestTemplate restTemplate;

  private transient String cometURL;

  @Autowired
  private Map<String, CometConverter> CometConverters;

  @Autowired
  public CometRestClientImpl(
      RestTemplate restTemplate,
      @Value("${service.job.baseUrl}") String baseUrl,
      @Value("${service.job.operation.jobs.create.path}") String jobServicePath) {
    this.restTemplate = restTemplate;
    this.cometURL = baseUrl + jobServicePath;
  }

  public void convertAndSendCreate(FWMTCreateJobRequest jobRequest) throws CTPException {
    final CometConverter cometConverter = CometConverters.get(jobRequest.getSurveyType());
    sendCreateJobRequest(cometConverter.convert(jobRequest));
  }

  public void sendCreateJobRequest(ModelCase modelCase) {
    try {
      MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
      bodyMap.add("request", modelCase);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);

      final HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(bodyMap, headers);
      restTemplate.exchange(cometURL, HttpMethod.POST, request, String.class);
    } catch (org.springframework.web.client.HttpClientErrorException HttpClientErrorException) {
      log.error("An error occurred while sending file to Job Service", HttpClientErrorException);
      throw new RuntimeException(HttpClientErrorException);
    }
  }
}

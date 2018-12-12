package uk.gov.ons.fwmt.census.jobservice.rest.client.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.census.jobservice.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.rest.client.CometRestClient;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

import java.util.Map;

import static uk.gov.ons.fwmt.census.jobservice.utils.JobServiceUtility.printJSON;

@Slf4j
@Component
public class CometRestClientImpl implements CometRestClient {

  private transient RestTemplate restTemplate;

  private transient String cometURL;

  @Autowired
  private Map<String, CometConverter> cometConverters;

  @Autowired
  public CometRestClientImpl(
      RestTemplate restTemplate,
      @Value("${service.mock.baseUrl}") String baseUrl,
      @Value("${service.mock.operation.case.create.path}") String mockPath) {
    this.restTemplate = restTemplate;
    this.cometURL = baseUrl + mockPath;
  }

  // TODO should this be in this class?
  public void convertAndSendCreate(FWMTCreateJobRequest jobRequest) throws CTPException {
    final CometConverter cometConverter = cometConverters.get(jobRequest.getSurveyType());
    sendCreateJobRequest(cometConverter.convert(jobRequest));
  }

  public void sendCreateJobRequest(ModelCase modelCase) {
    try {
      printJSON(modelCase);
      HttpEntity<ModelCase> body = new HttpEntity<>(modelCase);
      restTemplate.exchange(cometURL + modelCase.getId(), HttpMethod.POST, body, Void.class);
    } catch (org.springframework.web.client.HttpClientErrorException HttpClientErrorException) {
      log.error("An error occurred while sending file to Job Service", HttpClientErrorException);
      throw new RuntimeException(HttpClientErrorException);
    }
  }
}

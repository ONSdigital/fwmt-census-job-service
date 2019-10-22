package uk.gov.ons.census.fwmt.jobservice.health;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.TM_SERVICE_DOWN;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.TM_SERVICE_UP;

@Slf4j
@Component
public class TMHealthIndicator extends AbstractHealthIndicator {

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Value("${totalmobile.baseUrl}")
  private String tmBaseUrl;

  @Value("${totalmobile.username}")
  private String tmUsername;

  @Value("${totalmobile.password}")
  private String tmPassword;

  private String tmDownErrorDetails;

  @Override protected void doHealthCheck(Health.Builder builder) {
    int responseCode = 0;

    try {
      responseCode = getResponseFromTM();

      if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
        builder.up().withDetail(Integer.toString(responseCode), String.class);
        gatewayEventManager.triggerEvent("<N/A>", TM_SERVICE_UP, "response code", Integer.toString(responseCode));
        return;
      }
    } catch (Exception e) {
      builder.down().withDetail(e.getMessage(), Exception.class);
    }
    gatewayEventManager.triggerErrorEvent(this.getClass(), "Cannot reach TM", "<NA>", TM_SERVICE_DOWN);
  }

  private int getResponseFromTM(){
    String swaggerUrl;

    int response = 0;

    HttpHeaders headers = createBasicAuthHeaders(tmUsername, tmPassword);

    headers.setContentType(MediaType.APPLICATION_JSON);

    RestTemplate restTemplate = new RestTemplate();

    swaggerUrl = tmBaseUrl + "swagger-ui.html#/";

    HttpEntity<String> get = new HttpEntity<>(null, headers);

      ResponseEntity<Void> tmResponse = restTemplate.exchange(swaggerUrl, HttpMethod.GET, get, Void.class);
      response = tmResponse.getStatusCode().value();
      return response;
  }

  private HttpHeaders createBasicAuthHeaders(String username, String password) {
    HttpHeaders headers = new HttpHeaders();
    final String plainCreds = username + ":" + password;
    byte[] plainCredsBytes = plainCreds.getBytes();
    byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
    String base64Creds = new String(base64CredsBytes);
    headers.add("Authorization", "Basic " + base64Creds);
    return headers;
  }
}

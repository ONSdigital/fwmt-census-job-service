package uk.gov.ons.fwmt.census.jobservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.ons.fwmt.census.events.component.GatewayEventManager;

@Configuration
public class GatewayEventsConfig {

  public static final String CANONICAL_CREATE_JOB_RECEIVED = "Canonical - Create Job Received";
  public static final String CANONICAL_CANCEL_RECEIVED = "Canonical - Cancel Job Received";
  public static final String COMET_CREATE_SENT = "Comet - Create Job Request";
  public static final String COMET_OUTCOME_RECEIVED = "Comet - Case Outcome";

  @Bean
  public GatewayEventManager gatewayEventManager() {
    GatewayEventManager gatewayEventManager = new GatewayEventManager();
    gatewayEventManager.addEventTypes(new String[] {CANONICAL_CREATE_JOB_RECEIVED, CANONICAL_CANCEL_RECEIVED,
        COMET_CREATE_SENT, COMET_OUTCOME_RECEIVED});
    return gatewayEventManager;
  }
}

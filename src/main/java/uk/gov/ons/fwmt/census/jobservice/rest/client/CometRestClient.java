package uk.gov.ons.fwmt.census.jobservice.rest.client;

import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;

public interface CometRestClient {

  void sendCreateJobRequest(ModelCase modelCase) throws GatewayException;
}

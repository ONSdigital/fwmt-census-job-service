package uk.gov.ons.fwmt.census.jobservice.rest.client;

import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface CometRestClient {

  void sendCreateJobRequest(ModelCase modelCase) throws CTPException;
}

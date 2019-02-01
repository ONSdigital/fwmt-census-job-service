package uk.gov.ons.fwmt.census.jobservice.message;

import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;

public interface RMProducer {
  void send(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException;
}
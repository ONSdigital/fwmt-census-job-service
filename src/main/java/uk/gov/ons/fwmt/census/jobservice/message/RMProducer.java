package uk.gov.ons.fwmt.census.jobservice.message;

import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface RMProducer {
  void send(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws CTPException;
}
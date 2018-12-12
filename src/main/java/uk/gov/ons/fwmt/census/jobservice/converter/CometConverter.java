package uk.gov.ons.fwmt.census.jobservice.converter;

import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface CometConverter {
  ModelCase convert(FWMTCreateJobRequest ingest) throws CTPException;
}
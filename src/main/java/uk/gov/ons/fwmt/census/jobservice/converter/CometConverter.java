package uk.gov.ons.fwmt.census.jobservice.converter;

import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;

public interface CometConverter {
  ModelCase convert(CreateFieldWorkerJobRequest ingest) throws GatewayException;
}
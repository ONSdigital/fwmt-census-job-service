package uk.gov.ons.fwmt.census.jobservice.converter;

import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.data.modelcase.ModelCase;
import uk.gov.ons.fwmt.census.common.error.GatewayException;

public interface CometConverter {
  ModelCase convert(CreateFieldWorkerJobRequest ingest) throws GatewayException;
}
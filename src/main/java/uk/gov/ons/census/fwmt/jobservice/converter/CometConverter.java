package uk.gov.ons.census.fwmt.jobservice.converter;

import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface CometConverter {
  CaseRequest convert(CreateFieldWorkerJobRequest ingest) throws GatewayException;

  CasePauseRequest convertPause(CancelFieldWorkerJobRequest cancelIngest) throws GatewayException;
}
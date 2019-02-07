package uk.gov.ons.fwmt.census.jobservice.service;

import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;

public interface JobService {
  void createJob(CreateFieldWorkerJobRequest jobRequest) throws GatewayException;

  void cancelJob(CancelFieldWorkerJobRequest cancelRequest) throws GatewayException;

  void convertAndSendCreate(CreateFieldWorkerJobRequest jobRequest) throws GatewayException;

  void sendFeedback(CensusCaseOutcomeDTO caseOutcome) throws GatewayException;
}

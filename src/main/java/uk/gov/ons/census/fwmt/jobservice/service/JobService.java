package uk.gov.ons.census.fwmt.jobservice.service;

import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;

public interface JobService {
  void createJob(CreateFieldWorkerJobRequest jobRequest) throws GatewayException;

  void cancelJob(CancelFieldWorkerJobRequest cancelRequest) throws GatewayException;

  void convertAndSendCreate(CreateFieldWorkerJobRequest jobRequest) throws GatewayException;

  void convertAndSendCancel(CancelFieldWorkerJobRequest cancelJobRequest) throws GatewayException;
}
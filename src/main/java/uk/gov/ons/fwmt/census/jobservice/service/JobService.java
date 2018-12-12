package uk.gov.ons.fwmt.census.jobservice.service;

import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

public interface JobService {
  void createJob(FWMTCreateJobRequest jobRequest) throws CTPException;

  void cancelJob(FWMTCancelJobRequest cancelRequest) throws CTPException;

  void convertAndSendCreate(FWMTCreateJobRequest jobRequest) throws CTPException;

}

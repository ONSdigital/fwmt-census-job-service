package uk.gov.ons.fwmt.census.jobservice.service;

import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCancelJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;
import uk.gov.ons.fwmt.fwmtohsjobstatusnotification.FwmtOHSJobStatusNotification;

public interface JobService {
  void createJob(FWMTCreateJobRequest jobRequest) throws CTPException;

  void cancelJob(FWMTCancelJobRequest cancelRequest) throws CTPException;

  void notifyRM(FwmtOHSJobStatusNotification fwmtOHSJobStatusNotification) throws CTPException;

  void convertAndSendCreate(FWMTCreateJobRequest jobRequest) throws CTPException;
}

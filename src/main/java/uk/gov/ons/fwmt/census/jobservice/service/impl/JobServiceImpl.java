package uk.gov.ons.fwmt.census.jobservice.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.comet.dto.ModelCase;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.census.jobservice.rest.client.impl.CometRestClientImpl;
import uk.gov.ons.fwmt.census.jobservice.service.JobService;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;

@Service
public class JobServiceImpl implements JobService {
  @Autowired
  private CometRestClientImpl cometRestClient;

  @Autowired
  private Map<String, CometConverter> cometConverters;

  @Override
  public void createJob(CreateFieldWorkerJobRequest jobRequest) throws GatewayException {
    convertAndSendCreate(jobRequest);
  }

  @Override
  public void cancelJob(CancelFieldWorkerJobRequest cancelRequest) {
    // TODO implement once details are understood
  }

  @Override
  public void convertAndSendCreate(CreateFieldWorkerJobRequest jobRequest) throws GatewayException {
    final CometConverter cometConverter = cometConverters.get(jobRequest.getSurveyType());
    ModelCase modelCase = cometConverter.convert(jobRequest);
    cometRestClient.sendCreateJobRequest(modelCase);
  }

}

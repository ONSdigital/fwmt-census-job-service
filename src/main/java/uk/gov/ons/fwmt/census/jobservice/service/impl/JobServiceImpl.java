package uk.gov.ons.fwmt.census.jobservice.service.impl;

import static uk.gov.ons.fwmt.census.jobservice.config.GatewayEventsConfig.COMET_CREATE_SENT;
import static uk.gov.ons.fwmt.census.jobservice.config.GatewayEventsConfig.COMET_OUTCOME_RECEIVED;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.ons.fwmt.census.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.fwmt.census.common.data.modelcase.ModelCase;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.events.component.GatewayEventManager;
import uk.gov.ons.fwmt.census.jobservice.converter.CometConverter;
import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.jobservice.message.GatewayFeedbackProducer;
import uk.gov.ons.fwmt.census.jobservice.rest.client.CometRestClient;
import uk.gov.ons.fwmt.census.jobservice.service.JobService;

@Service
public class JobServiceImpl implements JobService {
  @Autowired
  private CometRestClient cometRestClient;

  @Autowired
  private Map<String, CometConverter> cometConverters;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  @Autowired
  private GatewayFeedbackProducer gatewayFeedbackProducer;

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
    gatewayEventManager.triggerEvent(modelCase.getId(), COMET_CREATE_SENT);
  }

  @Override
  public void sendFeedback(CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    gatewayFeedbackProducer.send(censusCaseOutcomeDTO);
    gatewayEventManager.triggerEvent(censusCaseOutcomeDTO.getCaseId(), COMET_OUTCOME_RECEIVED);
  }
}

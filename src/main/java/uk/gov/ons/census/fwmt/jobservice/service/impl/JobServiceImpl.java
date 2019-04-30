package uk.gov.ons.census.fwmt.jobservice.service.impl;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.vavr.CheckedFunction0;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.events.component.GatewayEventManager;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;
import uk.gov.ons.census.fwmt.jobservice.rest.client.CometRestClient;
import uk.gov.ons.census.fwmt.jobservice.service.JobService;

import java.time.LocalTime;
import java.util.Map;

import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_ACK;
import static uk.gov.ons.census.fwmt.jobservice.config.GatewayEventsConfig.COMET_CREATE_SENT;

@Service
@Slf4j
public class JobServiceImpl implements JobService {
  @Autowired
  private CometRestClient cometRestClient;

  @Autowired
  private Map<String, CometConverter> cometConverters;

  @Autowired
  private GatewayEventManager gatewayEventManager;

  private CircuitBreaker circuitBreaker;
  private Retry retry;

  public JobServiceImpl() {
    circuitBreaker = CircuitBreaker.ofDefaults("comet");
    retry = Retry.ofDefaults("comet");
  }

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
    CheckedFunction0<Void> func = () -> {
      try {
        final CometConverter cometConverter = cometConverters.get(jobRequest.getCaseType());
        CaseRequest caseRequest = cometConverter.convert(jobRequest);
        gatewayEventManager.triggerEvent(String.valueOf(jobRequest.getCaseId()), COMET_CREATE_SENT, LocalTime.now());
        cometRestClient.sendCreateJobRequest(caseRequest, String.valueOf(jobRequest.getCaseId()));
        gatewayEventManager.triggerEvent(String.valueOf(jobRequest.getCaseId()), COMET_CREATE_ACK, LocalTime.now());
      } catch (GatewayException e) {
        throw new RuntimeException(e);
      }
      return null;
    };

    doWithCircuitBreaker(func);
  }

  private <T> T doWithCircuitBreaker(CheckedFunction0<T> func) throws GatewayException {
    CheckedFunction0<T> decorated = CircuitBreaker.decorateCheckedSupplier(circuitBreaker, func);
    decorated = Retry.decorateCheckedSupplier(retry, decorated);

    Try<T> result = Try.of(decorated);

    if (result.isFailure()) {
      if (result.getCause().getCause() instanceof GatewayException) {
        throw (GatewayException) result.getCause().getCause();
      } else if (result.getCause() instanceof RuntimeException) {
        throw (RuntimeException) result.getCause();
      } else {
        throw new RuntimeException("Unexpected exception in JobServiceImpl", result.getCause());
      }
    }

    return result.get();
  }

  @Scheduled(fixedRate = 5000)
  private void checkTMConnectivity() {
    boolean isCometUp = cometRestClient.isUp();
    boolean isForcedClosed = circuitBreaker.getState() == CircuitBreaker.State.FORCED_OPEN;
    if (!isCometUp && !isForcedClosed) {
      log.warn("TM is down - Circuit Breaker opening");
      circuitBreaker.transitionToForcedOpenState();
    } else if (isCometUp && isForcedClosed) {
      log.warn("TM is up - Circuit Breaker half-closed");
      circuitBreaker.transitionToHalfOpenState();
    }
  }
}

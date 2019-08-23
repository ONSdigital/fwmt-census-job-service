package uk.gov.ons.census.fwmt.jobservice.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.jobservice.service.JobCacheManager;


@Component
public class CCSOutcomeStore {

  @Autowired
  private JobCacheManager jobCacheManager;

  public String retrieveCache(String caseId) {
    return jobCacheManager.getCachedCCSOutcome(caseId);
  }
}

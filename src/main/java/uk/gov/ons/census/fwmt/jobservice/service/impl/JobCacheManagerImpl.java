package uk.gov.ons.census.fwmt.jobservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.jobservice.config.RedisUtil;
import uk.gov.ons.census.fwmt.jobservice.service.JobCacheManager;

@Slf4j
@Service
public class JobCacheManagerImpl implements JobCacheManager {

  @Autowired
  private RedisUtil<String> redisUtil;

  @Override
  public String getCachedCCSOutcome(String caseId) {
    String output = String.valueOf(redisUtil.getValue(caseId));
    if (output != null) {
      log.info("Received object from cache: " + output);
    }
    return output;
  }
}

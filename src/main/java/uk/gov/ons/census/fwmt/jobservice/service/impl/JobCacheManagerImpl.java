package uk.gov.ons.census.fwmt.jobservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.census.fwmt.jobservice.config.RedisUtil;
import uk.gov.ons.census.fwmt.jobservice.entity.CCSOutcomeEntity;
import uk.gov.ons.census.fwmt.jobservice.service.JobCacheManager;

@Slf4j
@Service
public class JobCacheManagerImpl implements JobCacheManager {

  @Autowired
  private RedisUtil<CCSOutcomeEntity> redisUtil;

  @Override
  public CCSOutcomeEntity cacheCCSOutcome(CCSOutcomeEntity ccsOutcomeEntity) {
    redisUtil.putValue(ccsOutcomeEntity.getId(), ccsOutcomeEntity);
    log.info("Placed the following in cache: " +  ccsOutcomeEntity.toString());
    return ccsOutcomeEntity;
  }

  @Override
  public CCSOutcomeEntity getCachedCCSOutcome(String caseId) {
    CCSOutcomeEntity ccsOutcomeEntity =redisUtil.getValue(caseId);
    log.info("Received object from cache: " + ccsOutcomeEntity.toString());

    return ccsOutcomeEntity;
  }
}

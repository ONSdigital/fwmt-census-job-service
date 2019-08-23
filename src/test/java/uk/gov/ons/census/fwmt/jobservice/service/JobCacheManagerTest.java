package uk.gov.ons.census.fwmt.jobservice.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.jobservice.config.RedisUtil;
import uk.gov.ons.census.fwmt.jobservice.entity.CCSPLOutcomeEntity;
import uk.gov.ons.census.fwmt.jobservice.service.impl.JobCacheManagerImpl;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JobCacheManagerTest {

  @InjectMocks
  private JobCacheManagerImpl jobCacheManagerImpl;

  @Mock
  private RedisUtil<CCSPLOutcomeEntity> redisUtil;

  private String caseId = "8ed3fc08-e95f-44db-a6d7-cde4e76a6182";

  @Test
  public void getCachedHouseholdCaseIdTest() throws GatewayException {
    CCSPLOutcomeEntity ccsplOutcomeEntity = new CCSPLOutcomeEntity();
    ccsplOutcomeEntity.setCaseId(caseId);

    when(jobCacheManagerImpl.getCachedCCSOutcome(caseId)).thenReturn(anyString());

    jobCacheManagerImpl.getCachedCCSOutcome(caseId);

    verify(redisUtil).getValue(caseId);
  }
}

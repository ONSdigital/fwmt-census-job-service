package uk.gov.ons.census.fwmt.jobservice.service;

import uk.gov.ons.census.fwmt.jobservice.entity.CCSOutcomeEntity;

public interface JobCacheManager {

  CCSOutcomeEntity cacheCCSOutcome(CCSOutcomeEntity ccsOutcomeEntity);

  CCSOutcomeEntity getCachedCCSOutcome(String caseId);

}

package uk.gov.ons.census.fwmt.jobservice.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.jobservice.config.RedisUtil;

@Slf4j
@Component
public class CCSOutcomeStore {

  @Autowired
  private RedisUtil<String> redisUtil;

  public String retrieveCache(String caseId) {
    String output = String.valueOf(redisUtil.getValue(caseId));
    if (output != null) {
      log.info("Received object from cache with case ID: " + caseId);
    }
    return output;
  }
}

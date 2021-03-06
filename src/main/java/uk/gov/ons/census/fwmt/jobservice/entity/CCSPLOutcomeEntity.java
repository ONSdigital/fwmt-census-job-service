package uk.gov.ons.census.fwmt.jobservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("CCSPLOutcomeEntity")
public class CCSPLOutcomeEntity implements Serializable {

  private String caseId;

  private String ccsPropertyListingOutcome;

}

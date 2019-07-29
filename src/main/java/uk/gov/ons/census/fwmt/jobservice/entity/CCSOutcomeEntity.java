package uk.gov.ons.census.fwmt.jobservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("CCSOutcomeEntity")
public class CCSOutcomeEntity implements Serializable {

  private String id;

  private String jobJSON;

}

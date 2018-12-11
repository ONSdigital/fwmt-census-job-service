package uk.gov.ons.fwmt.census.jobservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Generated;

/**
 * LatLong
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-12-07T11:49:58.389925Z[Europe/London]")
@Data
@NoArgsConstructor
public class LatLong {
  private Double lat = null;
  @JsonProperty("long")
  private Double _long = null;
}


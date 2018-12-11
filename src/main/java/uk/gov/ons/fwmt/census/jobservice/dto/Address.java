package uk.gov.ons.fwmt.census.jobservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.List;

/**
 * Address
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-12-07T11:49:58.389925Z[Europe/London]")
@Data
@NoArgsConstructor
public class Address {
  private Long uprn = null;
  @Valid
  private List<String> lines = null;
  private String postCode = null;
}


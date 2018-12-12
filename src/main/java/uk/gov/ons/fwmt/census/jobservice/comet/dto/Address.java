package uk.gov.ons.fwmt.census.jobservice.comet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
public class Address {
  private Long uprn = null;
  @Valid
  private List<String> lines = null;
  private String postCode = null;
}


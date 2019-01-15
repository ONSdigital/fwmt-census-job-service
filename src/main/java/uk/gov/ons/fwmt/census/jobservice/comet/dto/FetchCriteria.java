package uk.gov.ons.fwmt.census.jobservice.comet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FetchCriteria {
  private String filter = null;
  private String order = null;
  private String include = null;
}


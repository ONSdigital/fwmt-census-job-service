package uk.gov.ons.fwmt.census.jobservice.data.dto;

import lombok.Data;

@Data
public class CensusCaseOutcomeDTO {
  private String caseId;
  private String caseReference;
  private String outcomeCategory;
  private String outcome;
  private String outcomeNote;
}

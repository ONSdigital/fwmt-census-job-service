package uk.gov.ons.fwmt.census.jobservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.jobservice.service.JobService;

@RestController
@RequestMapping("/CensusCaseOutcome")
public class CensusCaseOutcomeController {

  @Autowired
  private JobService jobService;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void censusCaseOutcomeResponse(@RequestBody CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws GatewayException {
    jobService.sendFeedback(censusCaseOutcomeDTO);
  }
}

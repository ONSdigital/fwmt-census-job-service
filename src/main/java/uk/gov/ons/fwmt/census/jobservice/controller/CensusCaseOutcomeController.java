package uk.gov.ons.fwmt.census.jobservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;

@Slf4j
@RestController
@RequestMapping("/CensusCaseOutcome")
public class CensusCaseOutcomeController {

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void CensusCaseOutcomeResponse(@RequestBody CensusCaseOutcomeDTO censusCaseOutcomeDTO) {
    log.info(censusCaseOutcomeDTO.toString());
  }

}

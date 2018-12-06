package uk.gov.ons.fwmt.census.jobservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.fwmt.census.jobservice.data.dto.CensusCaseOutcomeDTO;
import uk.gov.ons.fwmt.census.jobservice.message.RMProducer;
import uk.gov.ons.fwmt.fwmtgatewaycommon.error.CTPException;

@Slf4j
@RestController
@RequestMapping("/CensusCaseOutcome")
public class CensusCaseOutcomeController {

  @Autowired RMProducer rmProducer;

  @PostMapping(consumes = "application/json", produces = "application/json")
  public void CensusCaseOutcomeResponse(@RequestBody CensusCaseOutcomeDTO censusCaseOutcomeDTO) throws CTPException {
    log.info(censusCaseOutcomeDTO.toString());
    rmProducer.send(censusCaseOutcomeDTO);
  }

}

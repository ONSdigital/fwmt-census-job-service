package uk.gov.ons.fwmt.census.jobservice.converter;

import com.consiliumtechnologies.schemas.services.mobile._2009._03.messaging.SendCreateJobRequestMessage;

import uk.gov.ons.fwmt.census.common.error.GatewayException;
import uk.gov.ons.fwmt.census.canonical.v1.CreateFieldWorkerJobRequest;

public interface TMConverter {
  SendCreateJobRequestMessage convert(CreateFieldWorkerJobRequest ingest) throws GatewayException;
}

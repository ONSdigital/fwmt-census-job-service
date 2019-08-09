package uk.gov.ons.census.fwmt.jobservice.converter.impl;

import org.springframework.stereotype.Component;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.modelcase.CasePauseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Geography;
import uk.gov.ons.census.fwmt.common.data.modelcase.Location;
import uk.gov.ons.census.fwmt.common.data.modelcase.ModelCase;
import uk.gov.ons.census.fwmt.jobservice.converter.CometConverter;

import static uk.gov.ons.census.fwmt.common.data.modelcase.CaseRequest.TypeEnum.CCSPL;

@Component("CCS PL")
public class CCSPLConverter implements CometConverter {

  @Override
  public CaseRequest convert(CreateFieldWorkerJobRequest ingest) {
    CaseRequest caseRequest = new CaseRequest();
    Address address = new Address();
    Geography geography = new Geography();
    Location location = new Location();

    caseRequest.setReference(ingest.getCaseReference());
    caseRequest.setType(CCSPL);
    caseRequest.setSurveyType(ingest.getSurveyType());
    caseRequest.setEstabType(ingest.getEstablishmentType());
    caseRequest.setRequiredOfficer(ingest.getMandatoryResource());
    caseRequest.setCoordCode(ingest.getCoordinatorId());

    address.setPostcode(ingest.getAddress().getPostCode());

    geography.setOa(ingest.getAddress().getOa());
    address.setGeography(geography);

    caseRequest.setAddress(address);

    location.setLat(ingest.getAddress().getLatitude().floatValue());
    location.set_long(ingest.getAddress().getLongitude().floatValue());
    caseRequest.setLocation(location);

    return caseRequest;
  }

  @Override
  public CaseRequest convertUpdate(UpdateFieldWorkerJobRequest ingest, ModelCase modelCase) {
    throw new UnsupportedOperationException();
  }

  @Override
  public CasePauseRequest convertCancel(CancelFieldWorkerJobRequest cancelIngest) {
    throw new UnsupportedOperationException();
  }
}

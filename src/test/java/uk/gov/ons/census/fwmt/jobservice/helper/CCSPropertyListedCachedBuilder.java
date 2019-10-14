package uk.gov.ons.census.fwmt.jobservice.helper;

import uk.gov.ons.census.fwmt.canonical.v1.Address;
import uk.gov.ons.census.fwmt.canonical.v1.CancelFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.Contact;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.canonical.v1.UpdateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.ccs.CCSPropertyListingCached;
import uk.gov.ons.census.fwmt.common.data.ccs.CareCode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CCSPropertyListedCachedBuilder {

  List <CareCode> careCodes = new ArrayList();

  public CCSPropertyListingCached createCCSPropertyListedCache() {
    CCSPropertyListingCached ccsPropertyListingCached = new CCSPropertyListingCached();
    CareCode careCode = new CareCode();

    ccsPropertyListingCached.setAllocatedOfficer("TestMand");
    ccsPropertyListingCached.setPrimaryOutcome("Household");
    ccsPropertyListingCached.setSecondaryOutcome("Contact made");
    ccsPropertyListingCached.setManagerName("Mr Smith");
    ccsPropertyListingCached.setUsualResidents(10);
    ccsPropertyListingCached.setBedspaces(15);
    ccsPropertyListingCached.setContactPhone("0123456789");
    ccsPropertyListingCached.setOa("1234");
    ccsPropertyListingCached.setAccessInfo("Use gate");

    careCode.setCareCode("Mad dog");
    careCodes.add(careCode);
    ccsPropertyListingCached.setCareCodes(careCodes);

    return ccsPropertyListingCached;
  }

  }

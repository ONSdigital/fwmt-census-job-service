package uk.gov.ons.census.fwmt.jobservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import uk.gov.ons.census.fwmt.canonical.v1.CreateFieldWorkerJobRequest;
import uk.gov.ons.census.fwmt.common.data.modelcase.Address;
import uk.gov.ons.census.fwmt.common.data.modelcase.Geography;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class JobServiceUtils {

  private static String checkAddressLineNotBlank(List<String> addressLines, String addressLine) {
    if (StringUtils.isNotBlank((addressLine))) {
      addressLines.add(addressLine);
    }
    return addressLine;
  }

  public static List<String> addAddressLines(CreateFieldWorkerJobRequest ingest) {
    List<String> addressLines = new ArrayList<>();
    checkAddressLineNotBlank(addressLines, ingest.getAddress().getLine1());
    checkAddressLineNotBlank(addressLines, ingest.getAddress().getLine2());
    checkAddressLineNotBlank(addressLines, ingest.getAddress().getLine3());

    return addressLines;
  }

  public static Address setAddress(CreateFieldWorkerJobRequest ingest) {
    Address address = new Address();
    Geography geography = new Geography();

    // arin not yet part of Comet
    //    try {
    //      address.setArid(Long.valueOf(ingest.getAddress().getArid()));
    //    } catch (Exception e) {
    //      // if a problem resolving ARID, null is fine
    //    }
    Long uprn = null;
    try {
      uprn = Long.valueOf(ingest.getAddress().getUprn());
    } catch (NumberFormatException e) {
      // null is okay
    }
    address.setUprn(uprn);

    address.setTown(ingest.getAddress().getTownName());
    address.setPostcode(ingest.getAddress().getPostCode());

    geography.setOa(ingest.getAddress().getOa());
    address.setGeography(geography);

    return address;
  }

  public static <T> void printJSON(T javaObject) {
    ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
    try {
      String JSON = objectWriter.writeValueAsString(javaObject);
      log.debug(JSON);
    } catch (JsonProcessingException e) {
      log.error("Failed to process to JSON", e);
    }
  }
}

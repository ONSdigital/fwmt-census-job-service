package uk.gov.ons.fwmt.census.jobservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import uk.gov.ons.fwmt.fwmtgatewaycommon.data.FWMTCreateJobRequest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public final class JobServiceUtility {

  private static String checkAddressLineNotBlank(List<String> addressLines, String addressLine) {
    if (StringUtils.isNotBlank((addressLine))) {
      addressLines.add(addressLine);
    }
    return addressLine;
  }

  public static List<String> addAddressLines(FWMTCreateJobRequest ingest) {
    List<String> addressLines = new ArrayList<>();

    addressLines.add(checkAddressLineNotBlank(addressLines, ingest.getAddress().getLine1()));
    addressLines.add(checkAddressLineNotBlank(addressLines, ingest.getAddress().getLine2()));
    addressLines.add(checkAddressLineNotBlank(addressLines, ingest.getAddress().getLine3()));
    addressLines.add(checkAddressLineNotBlank(addressLines, ingest.getAddress().getLine4()));
    addressLines.add(checkAddressLineNotBlank(addressLines, ingest.getAddress().getTownName()));

    return addressLines;
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

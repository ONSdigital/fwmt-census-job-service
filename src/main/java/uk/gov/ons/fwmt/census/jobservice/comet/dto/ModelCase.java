package uk.gov.ons.fwmt.census.jobservice.comet.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.endpoint.web.Link;

import javax.validation.Valid;
import java.util.List;

@Data
@NoArgsConstructor
public class ModelCase {
  private String id = null;
  private String reference = null;
  private String caseType = null;
  private StateEnum state = null;
  private String category = null;
  private String estabType = null;
  private String coordCode = null;
  private Contact contact = null;
  private Address address = null;
  private LatLong location = null;
  private Integer htc = null;
  private Integer priority = null;
  private String description = null;
  private String specialInstructions = null;
  private String holdUntil = null;
  @Valid
  private List<Link> _links = null;

  public enum StateEnum {
    OPEN("open"),

    CLOSED("closed");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }
  }
}

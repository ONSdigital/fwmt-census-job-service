package uk.gov.ons.fwmt.census.jobservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Generated;
import javax.validation.Valid;
import java.util.List;

/**
 * ModelCase
 */
@Validated
@Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-12-07T11:49:58.389925Z[Europe/London]")
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

  /**
   * Case State
   */
  public enum StateEnum {
    OPEN("open"),

    CLOSED("closed");

    private String value;

    StateEnum(String value) {
      this.value = value;
    }
  }
}


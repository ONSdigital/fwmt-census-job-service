package uk.gov.ons.fwmt.census.jobservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Address
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-12-07T11:49:58.389925Z[Europe/London]")

public class Address {
  @JsonProperty("uprn")
  private Long uprn = null;

  @JsonProperty("lines")
  @Valid
  private List<String> lines = null;

  @JsonProperty("postCode")
  private String postCode = null;

  public Address uprn(Long uprn) {
    this.uprn = uprn;
    return this;
  }

  /**
   * Property UPRN
   *
   * @return uprn
   **/
  @ApiModelProperty(value = "Property UPRN")

  public Long getUprn() {
    return uprn;
  }

  public void setUprn(Long uprn) {
    this.uprn = uprn;
  }

  public Address lines(List<String> lines) {
    this.lines = lines;
    return this;
  }

  public Address addLinesItem(String linesItem) {
    if (this.lines == null) {
      this.lines = new ArrayList<String>();
    }
    this.lines.add(linesItem);
    return this;
  }

  /**
   * Address Lines
   *
   * @return lines
   **/
  @ApiModelProperty(value = "Address Lines")

  public List<String> getLines() {
    return lines;
  }

  public void setLines(List<String> lines) {
    this.lines = lines;
  }

  public Address postCode(String postCode) {
    this.postCode = postCode;
    return this;
  }

  /**
   * Address Postcode
   *
   * @return postCode
   **/
  @ApiModelProperty(required = true, value = "Address Postcode")
  @NotNull

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Address address = (Address) o;
    return Objects.equals(this.uprn, address.uprn) &&
        Objects.equals(this.lines, address.lines) &&
        Objects.equals(this.postCode, address.postCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uprn, lines, postCode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Address {\n");

    sb.append("    uprn: ").append(toIndentedString(uprn)).append("\n");
    sb.append("    lines: ").append(toIndentedString(lines)).append("\n");
    sb.append("    postCode: ").append(toIndentedString(postCode)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


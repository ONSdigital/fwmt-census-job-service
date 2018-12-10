package uk.gov.ons.fwmt.census.jobservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * LatLong
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-12-07T11:49:58.389925Z[Europe/London]")

public class LatLong {
  @JsonProperty("lat")
  private Double lat = null;

  @JsonProperty("long")
  private Double _long = null;

  public LatLong lat(Double lat) {
    this.lat = lat;
    return this;
  }

  /**
   * Latitude
   *
   * @return lat
   **/
  @ApiModelProperty(required = true, value = "Latitude")
  @NotNull

  public Double getLat() {
    return lat;
  }

  public void setLat(Double lat) {
    this.lat = lat;
  }

  public LatLong _long(Double _long) {
    this._long = _long;
    return this;
  }

  /**
   * Longitude
   *
   * @return _long
   **/
  @ApiModelProperty(required = true, value = "Longitude")
  @NotNull

  public Double getLong() {
    return _long;
  }

  public void setLong(Double _long) {
    this._long = _long;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LatLong latLong = (LatLong) o;
    return Objects.equals(this.lat, latLong.lat) &&
        Objects.equals(this._long, latLong._long);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lat, _long);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LatLong {\n");

    sb.append("    lat: ").append(toIndentedString(lat)).append("\n");
    sb.append("    _long: ").append(toIndentedString(_long)).append("\n");
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


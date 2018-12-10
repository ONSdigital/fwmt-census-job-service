package uk.gov.ons.fwmt.census.jobservice.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.boot.actuate.endpoint.web.Link;
import org.springframework.validation.annotation.Validated;
import org.threeten.bp.OffsetDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ModelCase
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2018-12-07T11:49:58.389925Z[Europe/London]")

public class ModelCase {
  @JsonProperty("id")
  private String id = null;

  @JsonProperty("reference")
  private String reference = null;

  @JsonProperty("caseType")
  private String caseType = null;
  @JsonProperty("state")
  private StateEnum state = null;
  @JsonProperty("category")
  private String category = null;
  @JsonProperty("estabType")
  private String estabType = null;
  @JsonProperty("coordCode")
  private String coordCode = null;
  @JsonProperty("contact")
  private Contact contact = null;
  @JsonProperty("address")
  private Address address = null;
  @JsonProperty("location")
  private LatLong location = null;
  @JsonProperty("htc")
  private Integer htc = null;
  @JsonProperty("priority")
  private Integer priority = null;
  @JsonProperty("description")
  private String description = null;
  @JsonProperty("specialInstructions")
  private String specialInstructions = null;
  @JsonProperty("holdUntil")
  private OffsetDateTime holdUntil = null;
  @JsonProperty("_links")
  @Valid
  private List<Link> _links = null;

  public ModelCase id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Case Identifier.
   *
   * @return id
   **/
  @ApiModelProperty(required = true, value = "Case Identifier.")
  @NotNull

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public ModelCase reference(String reference) {
    this.reference = reference;
    return this;
  }

  /**
   * Case Reference
   *
   * @return reference
   **/
  @ApiModelProperty(value = "Case Reference")

  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public ModelCase caseType(String caseType) {
    this.caseType = caseType;
    return this;
  }

  /**
   * Case Type
   *
   * @return caseType
   **/
  @ApiModelProperty(required = true, value = "Case Type")
  @NotNull

  public String getCaseType() {
    return caseType;
  }

  public void setCaseType(String caseType) {
    this.caseType = caseType;
  }

  public ModelCase state(StateEnum state) {
    this.state = state;
    return this;
  }

  /**
   * Case State
   *
   * @return state
   **/
  @ApiModelProperty(required = true, value = "Case State")
  @NotNull

  public StateEnum getState() {
    return state;
  }

  public void setState(StateEnum state) {
    this.state = state;
  }

  public ModelCase category(String category) {
    this.category = category;
    return this;
  }

  /**
   * Category
   *
   * @return category
   **/
  @ApiModelProperty(value = "Category")

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public ModelCase estabType(String estabType) {
    this.estabType = estabType;
    return this;
  }

  /**
   * Gets or sets the type of the estab.
   *
   * @return estabType
   **/
  @ApiModelProperty(value = "Gets or sets the type of the estab.")

  public String getEstabType() {
    return estabType;
  }

  public void setEstabType(String estabType) {
    this.estabType = estabType;
  }

  public ModelCase coordCode(String coordCode) {
    this.coordCode = coordCode;
    return this;
  }

  /**
   * Coordinate code
   *
   * @return coordCode
   **/
  @ApiModelProperty(required = true, value = "Coordinate code")
  @NotNull

  public String getCoordCode() {
    return coordCode;
  }

  public void setCoordCode(String coordCode) {
    this.coordCode = coordCode;
  }

  public ModelCase contact(Contact contact) {
    this.contact = contact;
    return this;
  }

  /**
   * Get contact
   *
   * @return contact
   **/
  @ApiModelProperty(value = "")

  @Valid

  public Contact getContact() {
    return contact;
  }

  public void setContact(Contact contact) {
    this.contact = contact;
  }

  public ModelCase address(Address address) {
    this.address = address;
    return this;
  }

  /**
   * Get address
   *
   * @return address
   **/
  @ApiModelProperty(value = "")

  @Valid

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public ModelCase location(LatLong location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
   *
   * @return location
   **/
  @ApiModelProperty(value = "")

  @Valid

  public LatLong getLocation() {
    return location;
  }

  public void setLocation(LatLong location) {
    this.location = location;
  }

  public ModelCase htc(Integer htc) {
    this.htc = htc;
    return this;
  }

  /**
   * Gets or sets the htc.
   *
   * @return htc
   **/
  @ApiModelProperty(value = "Gets or sets the htc.")

  public Integer getHtc() {
    return htc;
  }

  public void setHtc(Integer htc) {
    this.htc = htc;
  }

  public ModelCase priority(Integer priority) {
    this.priority = priority;
    return this;
  }

  /**
   * Priority
   *
   * @return priority
   **/
  @ApiModelProperty(value = "Priority")

  public Integer getPriority() {
    return priority;
  }

  public void setPriority(Integer priority) {
    this.priority = priority;
  }

  public ModelCase description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Description
   *
   * @return description
   **/
  @ApiModelProperty(value = "Description")

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ModelCase specialInstructions(String specialInstructions) {
    this.specialInstructions = specialInstructions;
    return this;
  }

  /**
   * Special Instructions
   *
   * @return specialInstructions
   **/
  @ApiModelProperty(value = "Special Instructions")

  public String getSpecialInstructions() {
    return specialInstructions;
  }

  public void setSpecialInstructions(String specialInstructions) {
    this.specialInstructions = specialInstructions;
  }

  public ModelCase holdUntil(OffsetDateTime holdUntil) {
    this.holdUntil = holdUntil;
    return this;
  }

  /**
   * HoldUntil
   *
   * @return holdUntil
   **/
  @ApiModelProperty(value = "HoldUntil")

  @Valid

  public OffsetDateTime getHoldUntil() {
    return holdUntil;
  }

  public void setHoldUntil(OffsetDateTime holdUntil) {
    this.holdUntil = holdUntil;
  }

  public ModelCase _links(List<Link> _links) {
    this._links = _links;
    return this;
  }

  public ModelCase addLinksItem(Link _linksItem) {
    if (this._links == null) {
      this._links = new ArrayList<Link>();
    }
    this._links.add(_linksItem);
    return this;
  }

  /**
   * Get _links
   *
   * @return _links
   **/
  @ApiModelProperty(value = "")

  @Valid

  public List<Link> getLinks() {
    return _links;
  }

  public void setLinks(List<Link> _links) {
    this._links = _links;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelCase _case = (ModelCase) o;
    return Objects.equals(this.id, _case.id) &&
        Objects.equals(this.reference, _case.reference) &&
        Objects.equals(this.caseType, _case.caseType) &&
        Objects.equals(this.state, _case.state) &&
        Objects.equals(this.category, _case.category) &&
        Objects.equals(this.estabType, _case.estabType) &&
        Objects.equals(this.coordCode, _case.coordCode) &&
        Objects.equals(this.contact, _case.contact) &&
        Objects.equals(this.address, _case.address) &&
        Objects.equals(this.location, _case.location) &&
        Objects.equals(this.htc, _case.htc) &&
        Objects.equals(this.priority, _case.priority) &&
        Objects.equals(this.description, _case.description) &&
        Objects.equals(this.specialInstructions, _case.specialInstructions) &&
        Objects.equals(this.holdUntil, _case.holdUntil) &&
        Objects.equals(this._links, _case._links);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, reference, caseType, state, category, estabType, coordCode, contact, address, location, htc, priority,
            description, specialInstructions, holdUntil, _links);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelCase {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    reference: ").append(toIndentedString(reference)).append("\n");
    sb.append("    caseType: ").append(toIndentedString(caseType)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    estabType: ").append(toIndentedString(estabType)).append("\n");
    sb.append("    coordCode: ").append(toIndentedString(coordCode)).append("\n");
    sb.append("    contact: ").append(toIndentedString(contact)).append("\n");
    sb.append("    address: ").append(toIndentedString(address)).append("\n");
    sb.append("    location: ").append(toIndentedString(location)).append("\n");
    sb.append("    htc: ").append(toIndentedString(htc)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    specialInstructions: ").append(toIndentedString(specialInstructions)).append("\n");
    sb.append("    holdUntil: ").append(toIndentedString(holdUntil)).append("\n");
    sb.append("    _links: ").append(toIndentedString(_links)).append("\n");
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

    @JsonCreator
    public static StateEnum fromValue(String text) {
      for (StateEnum b : StateEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }
}


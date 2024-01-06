/*
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.model;

import java.util.Objects;
import java.util.Arrays;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * SimpleUpgradeEffectDto
 */
@JsonPropertyOrder({
  SimpleUpgradeEffectDto.JSON_PROPERTY_DESCRIPTION,
  SimpleUpgradeEffectDto.JSON_PROPERTY_UPGRADE_MANIPULATOR
})
@JsonTypeName("SimpleUpgradeEffect")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public class SimpleUpgradeEffectDto {
  public static final String JSON_PROPERTY_DESCRIPTION = "description";
  private String description;

  /**
   * Gets or Sets upgradeManipulator
   */
  public enum UpgradeManipulatorEnum {
    NONE("NONE"),
    
    SLOTS("SLOTS"),
    
    DAMAGE("DAMAGE"),
    
    HIT("HIT"),
    
    INITIATIVE("INITIATIVE"),
    
    ARMOR("ARMOR"),
    
    WEIGHT("WEIGHT");

    private String value;

    UpgradeManipulatorEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static UpgradeManipulatorEnum fromValue(String value) {
      for (UpgradeManipulatorEnum b : UpgradeManipulatorEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  public static final String JSON_PROPERTY_UPGRADE_MANIPULATOR = "upgradeManipulator";
  private UpgradeManipulatorEnum upgradeManipulator;

  public SimpleUpgradeEffectDto() {
  }

  public SimpleUpgradeEffectDto description(String description) {
    
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getDescription() {
    return description;
  }


  @JsonProperty(JSON_PROPERTY_DESCRIPTION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setDescription(String description) {
    this.description = description;
  }


  public SimpleUpgradeEffectDto upgradeManipulator(UpgradeManipulatorEnum upgradeManipulator) {
    
    this.upgradeManipulator = upgradeManipulator;
    return this;
  }

   /**
   * Get upgradeManipulator
   * @return upgradeManipulator
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_UPGRADE_MANIPULATOR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UpgradeManipulatorEnum getUpgradeManipulator() {
    return upgradeManipulator;
  }


  @JsonProperty(JSON_PROPERTY_UPGRADE_MANIPULATOR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setUpgradeManipulator(UpgradeManipulatorEnum upgradeManipulator) {
    this.upgradeManipulator = upgradeManipulator;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SimpleUpgradeEffectDto simpleUpgradeEffect = (SimpleUpgradeEffectDto) o;
    return Objects.equals(this.description, simpleUpgradeEffect.description) &&
        Objects.equals(this.upgradeManipulator, simpleUpgradeEffect.upgradeManipulator);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, upgradeManipulator);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SimpleUpgradeEffectDto {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    upgradeManipulator: ").append(toIndentedString(upgradeManipulator)).append("\n");
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


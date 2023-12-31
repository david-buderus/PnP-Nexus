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
import org.openapitools.client.model.AdditiveUpgradeEffectDto;
import org.openapitools.client.model.MultiplicativeUpgradeEffectDto;
import org.openapitools.client.model.SimpleUpgradeEffectDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * UpgradeEffectsInnerDto
 */
@JsonPropertyOrder({
  UpgradeEffectsInnerDto.JSON_PROPERTY_DESCRIPTION,
  UpgradeEffectsInnerDto.JSON_PROPERTY_UPGRADE_MANIPULATOR,
  UpgradeEffectsInnerDto.JSON_PROPERTY_VALUE,
  UpgradeEffectsInnerDto.JSON_PROPERTY_FACTOR
})
@JsonTypeName("Upgrade_effects_inner")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public class UpgradeEffectsInnerDto {
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

  public static final String JSON_PROPERTY_VALUE = "value";
  private Float value;

  public static final String JSON_PROPERTY_FACTOR = "factor";
  private Float factor;

  public UpgradeEffectsInnerDto() {
  }

  public UpgradeEffectsInnerDto description(String description) {
    
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


  public UpgradeEffectsInnerDto upgradeManipulator(UpgradeManipulatorEnum upgradeManipulator) {
    
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


  public UpgradeEffectsInnerDto value(Float value) {
    
    this.value = value;
    return this;
  }

   /**
   * Get value
   * @return value
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Float getValue() {
    return value;
  }


  @JsonProperty(JSON_PROPERTY_VALUE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setValue(Float value) {
    this.value = value;
  }


  public UpgradeEffectsInnerDto factor(Float factor) {
    
    this.factor = factor;
    return this;
  }

   /**
   * Get factor
   * @return factor
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_FACTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Float getFactor() {
    return factor;
  }


  @JsonProperty(JSON_PROPERTY_FACTOR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setFactor(Float factor) {
    this.factor = factor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpgradeEffectsInnerDto upgradeEffectsInner = (UpgradeEffectsInnerDto) o;
    return Objects.equals(this.description, upgradeEffectsInner.description) &&
        Objects.equals(this.upgradeManipulator, upgradeEffectsInner.upgradeManipulator) &&
        Objects.equals(this.value, upgradeEffectsInner.value) &&
        Objects.equals(this.factor, upgradeEffectsInner.factor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, upgradeManipulator, value, factor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpgradeEffectsInnerDto {\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    upgradeManipulator: ").append(toIndentedString(upgradeManipulator)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    factor: ").append(toIndentedString(factor)).append("\n");
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


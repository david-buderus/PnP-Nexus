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
 * MultiplicativeUpgradeEffectAllOfDto
 */
@JsonPropertyOrder({
  MultiplicativeUpgradeEffectAllOfDto.JSON_PROPERTY_FACTOR
})
@JsonTypeName("MultiplicativeUpgradeEffect_allOf")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public class MultiplicativeUpgradeEffectAllOfDto {
  public static final String JSON_PROPERTY_FACTOR = "factor";
  private Float factor;

  public MultiplicativeUpgradeEffectAllOfDto() {
  }

  public MultiplicativeUpgradeEffectAllOfDto factor(Float factor) {
    
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
    MultiplicativeUpgradeEffectAllOfDto multiplicativeUpgradeEffectAllOf = (MultiplicativeUpgradeEffectAllOfDto) o;
    return Objects.equals(this.factor, multiplicativeUpgradeEffectAllOf.factor);
  }

  @Override
  public int hashCode() {
    return Objects.hash(factor);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MultiplicativeUpgradeEffectAllOfDto {\n");
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


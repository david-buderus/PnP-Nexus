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
import java.util.ArrayList;
import java.util.List;
import org.openapitools.client.model.PrimaryAttributeDependencyDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * SecondaryAttributeDto
 */
@JsonPropertyOrder({
  SecondaryAttributeDto.JSON_PROPERTY_CONSUMABLE,
  SecondaryAttributeDto.JSON_PROPERTY_ID,
  SecondaryAttributeDto.JSON_PROPERTY_NAME,
  SecondaryAttributeDto.JSON_PROPERTY_PRIMARY_ATTRIBUTE_DEPENDENCIES
})
@JsonTypeName("SecondaryAttribute")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public class SecondaryAttributeDto {
  public static final String JSON_PROPERTY_CONSUMABLE = "consumable";
  private Boolean consumable;

  public static final String JSON_PROPERTY_ID = "id";
  private String id;

  public static final String JSON_PROPERTY_NAME = "name";
  private String name;

  public static final String JSON_PROPERTY_PRIMARY_ATTRIBUTE_DEPENDENCIES = "primaryAttributeDependencies";
  private List<PrimaryAttributeDependencyDto> primaryAttributeDependencies = new ArrayList<>();

  public SecondaryAttributeDto() {
  }

  public SecondaryAttributeDto consumable(Boolean consumable) {
    
    this.consumable = consumable;
    return this;
  }

   /**
   * Get consumable
   * @return consumable
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_CONSUMABLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getConsumable() {
    return consumable;
  }


  @JsonProperty(JSON_PROPERTY_CONSUMABLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setConsumable(Boolean consumable) {
    this.consumable = consumable;
  }


  public SecondaryAttributeDto id(String id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @javax.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setId(String id) {
    this.id = id;
  }


  public SecondaryAttributeDto name(String name) {
    
    this.name = name;
    return this;
  }

   /**
   * Get name
   * @return name
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getName() {
    return name;
  }


  @JsonProperty(JSON_PROPERTY_NAME)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setName(String name) {
    this.name = name;
  }


  public SecondaryAttributeDto primaryAttributeDependencies(List<PrimaryAttributeDependencyDto> primaryAttributeDependencies) {
    
    this.primaryAttributeDependencies = primaryAttributeDependencies;
    return this;
  }

  public SecondaryAttributeDto addPrimaryAttributeDependenciesItem(PrimaryAttributeDependencyDto primaryAttributeDependenciesItem) {
    if (this.primaryAttributeDependencies == null) {
      this.primaryAttributeDependencies = new ArrayList<>();
    }
    this.primaryAttributeDependencies.add(primaryAttributeDependenciesItem);
    return this;
  }

   /**
   * Get primaryAttributeDependencies
   * @return primaryAttributeDependencies
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PRIMARY_ATTRIBUTE_DEPENDENCIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<PrimaryAttributeDependencyDto> getPrimaryAttributeDependencies() {
    return primaryAttributeDependencies;
  }


  @JsonProperty(JSON_PROPERTY_PRIMARY_ATTRIBUTE_DEPENDENCIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPrimaryAttributeDependencies(List<PrimaryAttributeDependencyDto> primaryAttributeDependencies) {
    this.primaryAttributeDependencies = primaryAttributeDependencies;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SecondaryAttributeDto secondaryAttribute = (SecondaryAttributeDto) o;
    return Objects.equals(this.consumable, secondaryAttribute.consumable) &&
        Objects.equals(this.id, secondaryAttribute.id) &&
        Objects.equals(this.name, secondaryAttribute.name) &&
        Objects.equals(this.primaryAttributeDependencies, secondaryAttribute.primaryAttributeDependencies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(consumable, id, name, primaryAttributeDependencies);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SecondaryAttributeDto {\n");
    sb.append("    consumable: ").append(toIndentedString(consumable)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    primaryAttributeDependencies: ").append(toIndentedString(primaryAttributeDependencies)).append("\n");
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

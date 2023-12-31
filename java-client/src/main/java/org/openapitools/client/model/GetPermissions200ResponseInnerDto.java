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
import org.openapitools.client.model.GrantedUniverseAuthorityDTODto;
import org.openapitools.client.model.RoleAuthorityDTODto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * GetPermissions200ResponseInnerDto
 */
@JsonPropertyOrder({
  GetPermissions200ResponseInnerDto.JSON_PROPERTY_PERMISSION,
  GetPermissions200ResponseInnerDto.JSON_PROPERTY_UNIVERSE,
  GetPermissions200ResponseInnerDto.JSON_PROPERTY_ROLE
})
@JsonTypeName("getPermissions_200_response_inner")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public class GetPermissions200ResponseInnerDto {
  public static final String JSON_PROPERTY_PERMISSION = "permission";
  private String permission;

  public static final String JSON_PROPERTY_UNIVERSE = "universe";
  private String universe;

  public static final String JSON_PROPERTY_ROLE = "role";
  private String role;

  public GetPermissions200ResponseInnerDto() {
  }

  public GetPermissions200ResponseInnerDto permission(String permission) {
    
    this.permission = permission;
    return this;
  }

   /**
   * Get permission
   * @return permission
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PERMISSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getPermission() {
    return permission;
  }


  @JsonProperty(JSON_PROPERTY_PERMISSION)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPermission(String permission) {
    this.permission = permission;
  }


  public GetPermissions200ResponseInnerDto universe(String universe) {
    
    this.universe = universe;
    return this;
  }

   /**
   * Get universe
   * @return universe
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_UNIVERSE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getUniverse() {
    return universe;
  }


  @JsonProperty(JSON_PROPERTY_UNIVERSE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setUniverse(String universe) {
    this.universe = universe;
  }


  public GetPermissions200ResponseInnerDto role(String role) {
    
    this.role = role;
    return this;
  }

   /**
   * Get role
   * @return role
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ROLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getRole() {
    return role;
  }


  @JsonProperty(JSON_PROPERTY_ROLE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetPermissions200ResponseInnerDto getPermissions200ResponseInner = (GetPermissions200ResponseInnerDto) o;
    return Objects.equals(this.permission, getPermissions200ResponseInner.permission) &&
        Objects.equals(this.universe, getPermissions200ResponseInner.universe) &&
        Objects.equals(this.role, getPermissions200ResponseInner.role);
  }

  @Override
  public int hashCode() {
    return Objects.hash(permission, universe, role);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetPermissions200ResponseInnerDto {\n");
    sb.append("    permission: ").append(toIndentedString(permission)).append("\n");
    sb.append("    universe: ").append(toIndentedString(universe)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
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


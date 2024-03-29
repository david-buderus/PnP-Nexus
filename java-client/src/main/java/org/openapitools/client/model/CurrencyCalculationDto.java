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
import org.openapitools.client.model.CurrencyCalculationEntryDto;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * CurrencyCalculationDto
 */
@JsonPropertyOrder({
  CurrencyCalculationDto.JSON_PROPERTY_BASE_CURRENCY,
  CurrencyCalculationDto.JSON_PROPERTY_BASE_CURRENCY_SHORT_FORM,
  CurrencyCalculationDto.JSON_PROPERTY_CALCULATION_ENTRIES
})
@JsonTypeName("CurrencyCalculation")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public class CurrencyCalculationDto {
  public static final String JSON_PROPERTY_BASE_CURRENCY = "baseCurrency";
  private String baseCurrency;

  public static final String JSON_PROPERTY_BASE_CURRENCY_SHORT_FORM = "baseCurrencyShortForm";
  private String baseCurrencyShortForm;

  public static final String JSON_PROPERTY_CALCULATION_ENTRIES = "calculationEntries";
  private List<CurrencyCalculationEntryDto> calculationEntries = new ArrayList<>();

  public CurrencyCalculationDto() {
  }

  public CurrencyCalculationDto baseCurrency(String baseCurrency) {
    
    this.baseCurrency = baseCurrency;
    return this;
  }

   /**
   * Get baseCurrency
   * @return baseCurrency
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BASE_CURRENCY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getBaseCurrency() {
    return baseCurrency;
  }


  @JsonProperty(JSON_PROPERTY_BASE_CURRENCY)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBaseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }


  public CurrencyCalculationDto baseCurrencyShortForm(String baseCurrencyShortForm) {
    
    this.baseCurrencyShortForm = baseCurrencyShortForm;
    return this;
  }

   /**
   * Get baseCurrencyShortForm
   * @return baseCurrencyShortForm
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BASE_CURRENCY_SHORT_FORM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getBaseCurrencyShortForm() {
    return baseCurrencyShortForm;
  }


  @JsonProperty(JSON_PROPERTY_BASE_CURRENCY_SHORT_FORM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBaseCurrencyShortForm(String baseCurrencyShortForm) {
    this.baseCurrencyShortForm = baseCurrencyShortForm;
  }


  public CurrencyCalculationDto calculationEntries(List<CurrencyCalculationEntryDto> calculationEntries) {
    
    this.calculationEntries = calculationEntries;
    return this;
  }

  public CurrencyCalculationDto addCalculationEntriesItem(CurrencyCalculationEntryDto calculationEntriesItem) {
    if (this.calculationEntries == null) {
      this.calculationEntries = new ArrayList<>();
    }
    this.calculationEntries.add(calculationEntriesItem);
    return this;
  }

   /**
   * Get calculationEntries
   * @return calculationEntries
  **/
  @javax.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_CALCULATION_ENTRIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public List<CurrencyCalculationEntryDto> getCalculationEntries() {
    return calculationEntries;
  }


  @JsonProperty(JSON_PROPERTY_CALCULATION_ENTRIES)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setCalculationEntries(List<CurrencyCalculationEntryDto> calculationEntries) {
    this.calculationEntries = calculationEntries;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrencyCalculationDto currencyCalculation = (CurrencyCalculationDto) o;
    return Objects.equals(this.baseCurrency, currencyCalculation.baseCurrency) &&
        Objects.equals(this.baseCurrencyShortForm, currencyCalculation.baseCurrencyShortForm) &&
        Objects.equals(this.calculationEntries, currencyCalculation.calculationEntries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(baseCurrency, baseCurrencyShortForm, calculationEntries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrencyCalculationDto {\n");
    sb.append("    baseCurrency: ").append(toIndentedString(baseCurrency)).append("\n");
    sb.append("    baseCurrencyShortForm: ").append(toIndentedString(baseCurrencyShortForm)).append("\n");
    sb.append("    calculationEntries: ").append(toIndentedString(calculationEntries)).append("\n");
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


package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.UpgradeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface UpgradeServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/upgrades?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllUpgrades(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllUpgrades</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/upgrades?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllUpgradesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllUpgrades</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllUpgradesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /api/{universe}/upgrades?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllUpgrades(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllUpgradesQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllUpgrades</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /api/{universe}/upgrades?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllUpgradesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllUpgradesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllUpgrades</code> method in a fluent style.
   */
  public static class DeleteAllUpgradesQueryParams extends HashMap<String, Object> {
    public DeleteAllUpgradesQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Deletes an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/{universe}/upgrades/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteUpgrade(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteUpgrade</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/{universe}/upgrades/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteUpgradeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;UpgradeDto&gt;
   */
  @RequestLine("GET /api/{universe}/upgrades?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<UpgradeDto> getAllUpgrades(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllUpgrades</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/upgrades?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<UpgradeDto>> getAllUpgradesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllUpgrades</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllUpgradesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;UpgradeDto&gt;
   */
  @RequestLine("GET /api/{universe}/upgrades?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<UpgradeDto> getAllUpgrades(@Param("universe") String universe, @QueryMap(encoded=true) GetAllUpgradesQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllUpgrades</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;UpgradeDto&gt;
      */
      @RequestLine("GET /api/{universe}/upgrades?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<UpgradeDto>> getAllUpgradesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllUpgradesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllUpgrades</code> method in a fluent style.
   */
  public static class GetAllUpgradesQueryParams extends HashMap<String, Object> {
    public GetAllUpgradesQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return UpgradeDto
   */
  @RequestLine("GET /api/{universe}/upgrades/{id}")
  @Headers({
    "Accept: */*",
  })
  UpgradeDto getUpgrade(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getUpgrade</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/upgrades/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<UpgradeDto> getUpgradeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param upgradeDto  (required)
   * @return List&lt;UpgradeDto&gt;
   */
  @RequestLine("POST /api/{universe}/upgrades")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<UpgradeDto> insertAllUpgrades(@Param("universe") String universe, List<UpgradeDto> upgradeDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllUpgrades</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param upgradeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/{universe}/upgrades")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<UpgradeDto>> insertAllUpgradesWithHttpInfo(@Param("universe") String universe, List<UpgradeDto> upgradeDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param upgradeDto  (required)
   * @return UpgradeDto
   */
  @RequestLine("PUT /api/{universe}/upgrades/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  UpgradeDto updateUpgrade(@Param("universe") String universe, @Param("id") String id, UpgradeDto upgradeDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateUpgrade</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param upgradeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/{universe}/upgrades/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<UpgradeDto> updateUpgradeWithHttpInfo(@Param("universe") String universe, @Param("id") String id, UpgradeDto upgradeDto);


}

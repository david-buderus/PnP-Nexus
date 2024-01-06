package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.PrimaryAttributeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface PrimaryAttributeServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/primary-attributes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllPrimaryAttributes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllPrimaryAttributes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/primary-attributes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllPrimaryAttributesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllPrimaryAttributes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllPrimaryAttributesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /{universe}/primary-attributes?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllPrimaryAttributes(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllPrimaryAttributesQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllPrimaryAttributes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /{universe}/primary-attributes?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllPrimaryAttributesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllPrimaryAttributesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllPrimaryAttributes</code> method in a fluent style.
   */
  public static class DeleteAllPrimaryAttributesQueryParams extends HashMap<String, Object> {
    public DeleteAllPrimaryAttributesQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /{universe}/primary-attributes/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deletePrimaryAttribute(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deletePrimaryAttribute</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /{universe}/primary-attributes/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deletePrimaryAttributeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;PrimaryAttributeDto&gt;
   */
  @RequestLine("GET /{universe}/primary-attributes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<PrimaryAttributeDto> getAllPrimaryAttributes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllPrimaryAttributes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/primary-attributes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<PrimaryAttributeDto>> getAllPrimaryAttributesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllPrimaryAttributes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllPrimaryAttributesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;PrimaryAttributeDto&gt;
   */
  @RequestLine("GET /{universe}/primary-attributes?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<PrimaryAttributeDto> getAllPrimaryAttributes(@Param("universe") String universe, @QueryMap(encoded=true) GetAllPrimaryAttributesQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllPrimaryAttributes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;PrimaryAttributeDto&gt;
      */
      @RequestLine("GET /{universe}/primary-attributes?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<PrimaryAttributeDto>> getAllPrimaryAttributesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllPrimaryAttributesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllPrimaryAttributes</code> method in a fluent style.
   */
  public static class GetAllPrimaryAttributesQueryParams extends HashMap<String, Object> {
    public GetAllPrimaryAttributesQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return PrimaryAttributeDto
   */
  @RequestLine("GET /{universe}/primary-attributes/{id}")
  @Headers({
    "Accept: */*",
  })
  PrimaryAttributeDto getPrimaryAttribute(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getPrimaryAttribute</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/primary-attributes/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<PrimaryAttributeDto> getPrimaryAttributeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param primaryAttributeDto  (required)
   * @return List&lt;PrimaryAttributeDto&gt;
   */
  @RequestLine("POST /{universe}/primary-attributes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<PrimaryAttributeDto> insertAllPrimaryAttributes(@Param("universe") String universe, List<PrimaryAttributeDto> primaryAttributeDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllPrimaryAttributes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param primaryAttributeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /{universe}/primary-attributes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<PrimaryAttributeDto>> insertAllPrimaryAttributesWithHttpInfo(@Param("universe") String universe, List<PrimaryAttributeDto> primaryAttributeDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param primaryAttributeDto  (required)
   * @return PrimaryAttributeDto
   */
  @RequestLine("PUT /{universe}/primary-attributes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  PrimaryAttributeDto updatePrimaryAttribute(@Param("universe") String universe, @Param("id") String id, PrimaryAttributeDto primaryAttributeDto);

  /**
   * Updates an object in the database
   * Similar to <code>updatePrimaryAttribute</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param primaryAttributeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /{universe}/primary-attributes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<PrimaryAttributeDto> updatePrimaryAttributeWithHttpInfo(@Param("universe") String universe, @Param("id") String id, PrimaryAttributeDto primaryAttributeDto);


}

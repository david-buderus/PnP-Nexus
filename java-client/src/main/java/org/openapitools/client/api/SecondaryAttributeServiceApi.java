package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.SecondaryAttributeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface SecondaryAttributeServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/secondary-attributes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllSecondaryAttributes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllSecondaryAttributes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/secondary-attributes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllSecondaryAttributesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllSecondaryAttributes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllSecondaryAttributesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /{universe}/secondary-attributes?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllSecondaryAttributes(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllSecondaryAttributesQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllSecondaryAttributes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /{universe}/secondary-attributes?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllSecondaryAttributesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllSecondaryAttributesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllSecondaryAttributes</code> method in a fluent style.
   */
  public static class DeleteAllSecondaryAttributesQueryParams extends HashMap<String, Object> {
    public DeleteAllSecondaryAttributesQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /{universe}/secondary-attributes/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteSecondaryAttribute(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteSecondaryAttribute</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /{universe}/secondary-attributes/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteSecondaryAttributeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;SecondaryAttributeDto&gt;
   */
  @RequestLine("GET /{universe}/secondary-attributes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<SecondaryAttributeDto> getAllSecondaryAttributes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllSecondaryAttributes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/secondary-attributes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<SecondaryAttributeDto>> getAllSecondaryAttributesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllSecondaryAttributes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllSecondaryAttributesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;SecondaryAttributeDto&gt;
   */
  @RequestLine("GET /{universe}/secondary-attributes?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<SecondaryAttributeDto> getAllSecondaryAttributes(@Param("universe") String universe, @QueryMap(encoded=true) GetAllSecondaryAttributesQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllSecondaryAttributes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;SecondaryAttributeDto&gt;
      */
      @RequestLine("GET /{universe}/secondary-attributes?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<SecondaryAttributeDto>> getAllSecondaryAttributesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllSecondaryAttributesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllSecondaryAttributes</code> method in a fluent style.
   */
  public static class GetAllSecondaryAttributesQueryParams extends HashMap<String, Object> {
    public GetAllSecondaryAttributesQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return SecondaryAttributeDto
   */
  @RequestLine("GET /{universe}/secondary-attributes/{id}")
  @Headers({
    "Accept: */*",
  })
  SecondaryAttributeDto getSecondaryAttribute(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getSecondaryAttribute</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/secondary-attributes/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<SecondaryAttributeDto> getSecondaryAttributeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param secondaryAttributeDto  (required)
   * @return List&lt;SecondaryAttributeDto&gt;
   */
  @RequestLine("POST /{universe}/secondary-attributes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<SecondaryAttributeDto> insertAllSecondaryAttributes(@Param("universe") String universe, List<SecondaryAttributeDto> secondaryAttributeDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllSecondaryAttributes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param secondaryAttributeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /{universe}/secondary-attributes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<SecondaryAttributeDto>> insertAllSecondaryAttributesWithHttpInfo(@Param("universe") String universe, List<SecondaryAttributeDto> secondaryAttributeDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param secondaryAttributeDto  (required)
   * @return SecondaryAttributeDto
   */
  @RequestLine("PUT /{universe}/secondary-attributes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  SecondaryAttributeDto updateSecondaryAttribute(@Param("universe") String universe, @Param("id") String id, SecondaryAttributeDto secondaryAttributeDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateSecondaryAttribute</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param secondaryAttributeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /{universe}/secondary-attributes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<SecondaryAttributeDto> updateSecondaryAttributeWithHttpInfo(@Param("universe") String universe, @Param("id") String id, SecondaryAttributeDto secondaryAttributeDto);


}

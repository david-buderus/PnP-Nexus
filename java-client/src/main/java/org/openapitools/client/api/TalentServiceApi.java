package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.TalentDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface TalentServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/talents?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllTalents(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllTalents</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/talents?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllTalentsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllTalents</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllTalentsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /{universe}/talents?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllTalents(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllTalentsQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllTalents</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /{universe}/talents?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllTalentsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllTalentsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllTalents</code> method in a fluent style.
   */
  public static class DeleteAllTalentsQueryParams extends HashMap<String, Object> {
    public DeleteAllTalentsQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /{universe}/talents/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteTalent(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteTalent</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /{universe}/talents/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteTalentWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;TalentDto&gt;
   */
  @RequestLine("GET /{universe}/talents?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<TalentDto> getAllTalents(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllTalents</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/talents?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<TalentDto>> getAllTalentsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllTalents</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllTalentsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;TalentDto&gt;
   */
  @RequestLine("GET /{universe}/talents?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<TalentDto> getAllTalents(@Param("universe") String universe, @QueryMap(encoded=true) GetAllTalentsQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllTalents</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;TalentDto&gt;
      */
      @RequestLine("GET /{universe}/talents?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<TalentDto>> getAllTalentsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllTalentsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllTalents</code> method in a fluent style.
   */
  public static class GetAllTalentsQueryParams extends HashMap<String, Object> {
    public GetAllTalentsQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return TalentDto
   */
  @RequestLine("GET /{universe}/talents/{id}")
  @Headers({
    "Accept: */*",
  })
  TalentDto getTalent(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getTalent</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/talents/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<TalentDto> getTalentWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param talentDto  (required)
   * @return List&lt;TalentDto&gt;
   */
  @RequestLine("POST /{universe}/talents")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<TalentDto> insertAllTalents(@Param("universe") String universe, List<TalentDto> talentDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllTalents</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param talentDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /{universe}/talents")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<TalentDto>> insertAllTalentsWithHttpInfo(@Param("universe") String universe, List<TalentDto> talentDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param talentDto  (required)
   * @return TalentDto
   */
  @RequestLine("PUT /{universe}/talents/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  TalentDto updateTalent(@Param("universe") String universe, @Param("id") String id, TalentDto talentDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateTalent</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param talentDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /{universe}/talents/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<TalentDto> updateTalentWithHttpInfo(@Param("universe") String universe, @Param("id") String id, TalentDto talentDto);


}

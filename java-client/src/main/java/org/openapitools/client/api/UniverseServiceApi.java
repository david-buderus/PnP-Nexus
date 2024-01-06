package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.UniverseDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface UniverseServiceApi extends ApiClient.Api {


  /**
   * Add the given access right to the given user
   * 
   * @param universe  (required)
   * @param username  (required)
   * @param accessPermission  (optional, default to READ)
   */
  @RequestLine("POST /api/universes/{universe}/permission?username={username}&accessPermission={accessPermission}")
  @Headers({
    "Accept: application/json",
  })
  void addPermission(@Param("universe") String universe, @Param("username") String username, @Param("accessPermission") String accessPermission);

  /**
   * Add the given access right to the given user
   * Similar to <code>addPermission</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param username  (required)
   * @param accessPermission  (optional, default to READ)
   */
  @RequestLine("POST /api/universes/{universe}/permission?username={username}&accessPermission={accessPermission}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> addPermissionWithHttpInfo(@Param("universe") String universe, @Param("username") String username, @Param("accessPermission") String accessPermission);


  /**
   * Add the given access right to the given user
   * 
   * Note, this is equivalent to the other <code>addPermission</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link AddPermissionQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>username -  (required)</li>
   *   <li>accessPermission -  (optional, default to READ)</li>
   *   </ul>
   */
  @RequestLine("POST /api/universes/{universe}/permission?username={username}&accessPermission={accessPermission}")
  @Headers({
  "Accept: application/json",
  })
  void addPermission(@Param("universe") String universe, @QueryMap(encoded=true) AddPermissionQueryParams queryParams);

  /**
  * Add the given access right to the given user
  * 
  * Note, this is equivalent to the other <code>addPermission</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>username -  (required)</li>
          *   <li>accessPermission -  (optional, default to READ)</li>
      *   </ul>
      */
      @RequestLine("POST /api/universes/{universe}/permission?username={username}&accessPermission={accessPermission}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> addPermissionWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) AddPermissionQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>addPermission</code> method in a fluent style.
   */
  public static class AddPermissionQueryParams extends HashMap<String, Object> {
    public AddPermissionQueryParams username(final String value) {
      put("username", EncodingUtils.encode(value));
      return this;
    }
    public AddPermissionQueryParams accessPermission(final String value) {
      put("accessPermission", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Create a universe
   * 
   * @param universeDto  (required)
   * @return UniverseDto
   */
  @RequestLine("POST /api/universes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  UniverseDto createUniverse(UniverseDto universeDto);

  /**
   * Create a universe
   * Similar to <code>createUniverse</code> but it also returns the http response headers .
   * 
   * @param universeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/universes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<UniverseDto> createUniverseWithHttpInfo(UniverseDto universeDto);



  /**
   * Delete a universe
   * 
   * @param universe  (required)
   */
  @RequestLine("DELETE /api/universes/{universe}")
  @Headers({
    "Accept: application/json",
  })
  void deleteUniverse(@Param("universe") String universe);

  /**
   * Delete a universe
   * Similar to <code>deleteUniverse</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   */
  @RequestLine("DELETE /api/universes/{universe}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteUniverseWithHttpInfo(@Param("universe") String universe);



  /**
   * Get all Universes
   * 
   * @return List&lt;UniverseDto&gt;
   */
  @RequestLine("GET /api/universes")
  @Headers({
    "Accept: */*",
  })
  List<UniverseDto> getAllUniverses();

  /**
   * Get all Universes
   * Similar to <code>getAllUniverses</code> but it also returns the http response headers .
   * 
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/universes")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<UniverseDto>> getAllUniversesWithHttpInfo();



  /**
   * Get a universe
   * 
   * @param universe  (required)
   * @return UniverseDto
   */
  @RequestLine("GET /api/universes/{universe}")
  @Headers({
    "Accept: */*",
  })
  UniverseDto getUniverse(@Param("universe") String universe);

  /**
   * Get a universe
   * Similar to <code>getUniverse</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/universes/{universe}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<UniverseDto> getUniverseWithHttpInfo(@Param("universe") String universe);



  /**
   * Removes all access rights to the universe from the given user
   * 
   * @param universe  (required)
   * @param username  (required)
   */
  @RequestLine("DELETE /api/universes/{universe}/permission?username={username}")
  @Headers({
    "Accept: application/json",
  })
  void removePermission(@Param("universe") String universe, @Param("username") String username);

  /**
   * Removes all access rights to the universe from the given user
   * Similar to <code>removePermission</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param username  (required)
   */
  @RequestLine("DELETE /api/universes/{universe}/permission?username={username}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> removePermissionWithHttpInfo(@Param("universe") String universe, @Param("username") String username);


  /**
   * Removes all access rights to the universe from the given user
   * 
   * Note, this is equivalent to the other <code>removePermission</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link RemovePermissionQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>username -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /api/universes/{universe}/permission?username={username}")
  @Headers({
  "Accept: application/json",
  })
  void removePermission(@Param("universe") String universe, @QueryMap(encoded=true) RemovePermissionQueryParams queryParams);

  /**
  * Removes all access rights to the universe from the given user
  * 
  * Note, this is equivalent to the other <code>removePermission</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>username -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /api/universes/{universe}/permission?username={username}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> removePermissionWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) RemovePermissionQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>removePermission</code> method in a fluent style.
   */
  public static class RemovePermissionQueryParams extends HashMap<String, Object> {
    public RemovePermissionQueryParams username(final String value) {
      put("username", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Update a universe
   * 
   * @param universe  (required)
   * @param universeDto  (required)
   * @return UniverseDto
   */
  @RequestLine("PUT /api/universes/{universe}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  UniverseDto updateUniverse(@Param("universe") String universe, UniverseDto universeDto);

  /**
   * Update a universe
   * Similar to <code>updateUniverse</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param universeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/universes/{universe}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<UniverseDto> updateUniverseWithHttpInfo(@Param("universe") String universe, UniverseDto universeDto);


}

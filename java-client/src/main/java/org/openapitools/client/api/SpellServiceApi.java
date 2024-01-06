package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.SpellDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface SpellServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/spells?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllSpells(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllSpells</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/spells?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllSpellsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllSpells</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllSpellsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /{universe}/spells?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllSpells(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllSpellsQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllSpells</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /{universe}/spells?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllSpellsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllSpellsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllSpells</code> method in a fluent style.
   */
  public static class DeleteAllSpellsQueryParams extends HashMap<String, Object> {
    public DeleteAllSpellsQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /{universe}/spells/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteSpell(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteSpell</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /{universe}/spells/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteSpellWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;SpellDto&gt;
   */
  @RequestLine("GET /{universe}/spells?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<SpellDto> getAllSpells(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllSpells</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/spells?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<SpellDto>> getAllSpellsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllSpells</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllSpellsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;SpellDto&gt;
   */
  @RequestLine("GET /{universe}/spells?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<SpellDto> getAllSpells(@Param("universe") String universe, @QueryMap(encoded=true) GetAllSpellsQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllSpells</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;SpellDto&gt;
      */
      @RequestLine("GET /{universe}/spells?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<SpellDto>> getAllSpellsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllSpellsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllSpells</code> method in a fluent style.
   */
  public static class GetAllSpellsQueryParams extends HashMap<String, Object> {
    public GetAllSpellsQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return SpellDto
   */
  @RequestLine("GET /{universe}/spells/{id}")
  @Headers({
    "Accept: */*",
  })
  SpellDto getSpell(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getSpell</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/spells/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<SpellDto> getSpellWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param spellDto  (required)
   * @return List&lt;SpellDto&gt;
   */
  @RequestLine("POST /{universe}/spells")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<SpellDto> insertAllSpells(@Param("universe") String universe, List<SpellDto> spellDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllSpells</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param spellDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /{universe}/spells")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<SpellDto>> insertAllSpellsWithHttpInfo(@Param("universe") String universe, List<SpellDto> spellDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param spellDto  (required)
   * @return SpellDto
   */
  @RequestLine("PUT /{universe}/spells/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  SpellDto updateSpell(@Param("universe") String universe, @Param("id") String id, SpellDto spellDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateSpell</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param spellDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /{universe}/spells/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<SpellDto> updateSpellWithHttpInfo(@Param("universe") String universe, @Param("id") String id, SpellDto spellDto);


}

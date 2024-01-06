package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.ItemTypeTranslationDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface ItemTypeTranslationServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/item-type-translations?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllItemTypeTranslations(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllItemTypeTranslations</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/item-type-translations?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllItemTypeTranslationsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllItemTypeTranslations</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllItemTypeTranslationsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /api/{universe}/item-type-translations?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllItemTypeTranslations(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllItemTypeTranslationsQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllItemTypeTranslations</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /api/{universe}/item-type-translations?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllItemTypeTranslationsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllItemTypeTranslationsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllItemTypeTranslations</code> method in a fluent style.
   */
  public static class DeleteAllItemTypeTranslationsQueryParams extends HashMap<String, Object> {
    public DeleteAllItemTypeTranslationsQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /api/{universe}/item-type-translations/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteItemTypeTranslation(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteItemTypeTranslation</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/{universe}/item-type-translations/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteItemTypeTranslationWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;ItemTypeTranslationDto&gt;
   */
  @RequestLine("GET /api/{universe}/item-type-translations?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<ItemTypeTranslationDto> getAllItemTypeTranslations(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllItemTypeTranslations</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/item-type-translations?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<ItemTypeTranslationDto>> getAllItemTypeTranslationsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllItemTypeTranslations</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllItemTypeTranslationsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;ItemTypeTranslationDto&gt;
   */
  @RequestLine("GET /api/{universe}/item-type-translations?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<ItemTypeTranslationDto> getAllItemTypeTranslations(@Param("universe") String universe, @QueryMap(encoded=true) GetAllItemTypeTranslationsQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllItemTypeTranslations</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;ItemTypeTranslationDto&gt;
      */
      @RequestLine("GET /api/{universe}/item-type-translations?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<ItemTypeTranslationDto>> getAllItemTypeTranslationsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllItemTypeTranslationsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllItemTypeTranslations</code> method in a fluent style.
   */
  public static class GetAllItemTypeTranslationsQueryParams extends HashMap<String, Object> {
    public GetAllItemTypeTranslationsQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return ItemTypeTranslationDto
   */
  @RequestLine("GET /api/{universe}/item-type-translations/{id}")
  @Headers({
    "Accept: */*",
  })
  ItemTypeTranslationDto getItemTypeTranslation(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getItemTypeTranslation</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/item-type-translations/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<ItemTypeTranslationDto> getItemTypeTranslationWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param itemTypeTranslationDto  (required)
   * @return List&lt;ItemTypeTranslationDto&gt;
   */
  @RequestLine("POST /api/{universe}/item-type-translations")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<ItemTypeTranslationDto> insertAllItemTypeTranslations(@Param("universe") String universe, List<ItemTypeTranslationDto> itemTypeTranslationDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllItemTypeTranslations</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param itemTypeTranslationDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/{universe}/item-type-translations")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<ItemTypeTranslationDto>> insertAllItemTypeTranslationsWithHttpInfo(@Param("universe") String universe, List<ItemTypeTranslationDto> itemTypeTranslationDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param itemTypeTranslationDto  (required)
   * @return ItemTypeTranslationDto
   */
  @RequestLine("PUT /api/{universe}/item-type-translations/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ItemTypeTranslationDto updateItemTypeTranslation(@Param("universe") String universe, @Param("id") String id, ItemTypeTranslationDto itemTypeTranslationDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateItemTypeTranslation</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param itemTypeTranslationDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/{universe}/item-type-translations/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<ItemTypeTranslationDto> updateItemTypeTranslationWithHttpInfo(@Param("universe") String universe, @Param("id") String id, ItemTypeTranslationDto itemTypeTranslationDto);


}

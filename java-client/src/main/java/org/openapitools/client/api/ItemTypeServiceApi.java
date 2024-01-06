package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.ItemTypeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface ItemTypeServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/item-types?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllItemTypes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllItemTypes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/item-types?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllItemTypesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllItemTypes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllItemTypesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /api/{universe}/item-types?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllItemTypes(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllItemTypesQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllItemTypes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /api/{universe}/item-types?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllItemTypesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllItemTypesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllItemTypes</code> method in a fluent style.
   */
  public static class DeleteAllItemTypesQueryParams extends HashMap<String, Object> {
    public DeleteAllItemTypesQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /api/{universe}/item-types/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteItemType(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteItemType</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/{universe}/item-types/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteItemTypeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;ItemTypeDto&gt;
   */
  @RequestLine("GET /api/{universe}/item-types?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<ItemTypeDto> getAllItemTypes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllItemTypes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/item-types?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<ItemTypeDto>> getAllItemTypesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllItemTypes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllItemTypesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;ItemTypeDto&gt;
   */
  @RequestLine("GET /api/{universe}/item-types?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<ItemTypeDto> getAllItemTypes(@Param("universe") String universe, @QueryMap(encoded=true) GetAllItemTypesQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllItemTypes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;ItemTypeDto&gt;
      */
      @RequestLine("GET /api/{universe}/item-types?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<ItemTypeDto>> getAllItemTypesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllItemTypesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllItemTypes</code> method in a fluent style.
   */
  public static class GetAllItemTypesQueryParams extends HashMap<String, Object> {
    public GetAllItemTypesQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return ItemTypeDto
   */
  @RequestLine("GET /api/{universe}/item-types/{id}")
  @Headers({
    "Accept: */*",
  })
  ItemTypeDto getItemType(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getItemType</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/item-types/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<ItemTypeDto> getItemTypeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param itemTypeDto  (required)
   * @return List&lt;ItemTypeDto&gt;
   */
  @RequestLine("POST /api/{universe}/item-types")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<ItemTypeDto> insertAllItemTypes(@Param("universe") String universe, List<ItemTypeDto> itemTypeDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllItemTypes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param itemTypeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/{universe}/item-types")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<ItemTypeDto>> insertAllItemTypesWithHttpInfo(@Param("universe") String universe, List<ItemTypeDto> itemTypeDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param itemTypeDto  (required)
   * @return ItemTypeDto
   */
  @RequestLine("PUT /api/{universe}/item-types/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ItemTypeDto updateItemType(@Param("universe") String universe, @Param("id") String id, ItemTypeDto itemTypeDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateItemType</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param itemTypeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/{universe}/item-types/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<ItemTypeDto> updateItemTypeWithHttpInfo(@Param("universe") String universe, @Param("id") String id, ItemTypeDto itemTypeDto);


}

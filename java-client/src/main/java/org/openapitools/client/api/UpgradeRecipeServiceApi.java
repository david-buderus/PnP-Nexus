package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.UpgradeRecipeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface UpgradeRecipeServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/upgrade-recipes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllUpgradeRecipes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllUpgradeRecipes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /api/{universe}/upgrade-recipes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllUpgradeRecipesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllUpgradeRecipes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllUpgradeRecipesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /api/{universe}/upgrade-recipes?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllUpgradeRecipes(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllUpgradeRecipesQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllUpgradeRecipes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /api/{universe}/upgrade-recipes?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllUpgradeRecipesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllUpgradeRecipesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllUpgradeRecipes</code> method in a fluent style.
   */
  public static class DeleteAllUpgradeRecipesQueryParams extends HashMap<String, Object> {
    public DeleteAllUpgradeRecipesQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /api/{universe}/upgrade-recipes/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteUpgradeRecipe(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteUpgradeRecipe</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /api/{universe}/upgrade-recipes/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteUpgradeRecipeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;UpgradeRecipeDto&gt;
   */
  @RequestLine("GET /api/{universe}/upgrade-recipes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<UpgradeRecipeDto> getAllUpgradeRecipes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllUpgradeRecipes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/upgrade-recipes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<UpgradeRecipeDto>> getAllUpgradeRecipesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllUpgradeRecipes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllUpgradeRecipesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;UpgradeRecipeDto&gt;
   */
  @RequestLine("GET /api/{universe}/upgrade-recipes?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<UpgradeRecipeDto> getAllUpgradeRecipes(@Param("universe") String universe, @QueryMap(encoded=true) GetAllUpgradeRecipesQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllUpgradeRecipes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;UpgradeRecipeDto&gt;
      */
      @RequestLine("GET /api/{universe}/upgrade-recipes?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<UpgradeRecipeDto>> getAllUpgradeRecipesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllUpgradeRecipesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllUpgradeRecipes</code> method in a fluent style.
   */
  public static class GetAllUpgradeRecipesQueryParams extends HashMap<String, Object> {
    public GetAllUpgradeRecipesQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return UpgradeRecipeDto
   */
  @RequestLine("GET /api/{universe}/upgrade-recipes/{id}")
  @Headers({
    "Accept: */*",
  })
  UpgradeRecipeDto getUpgradeRecipe(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getUpgradeRecipe</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/{universe}/upgrade-recipes/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<UpgradeRecipeDto> getUpgradeRecipeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param upgradeRecipeDto  (required)
   * @return List&lt;UpgradeRecipeDto&gt;
   */
  @RequestLine("POST /api/{universe}/upgrade-recipes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<UpgradeRecipeDto> insertAllUpgradeRecipes(@Param("universe") String universe, List<UpgradeRecipeDto> upgradeRecipeDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllUpgradeRecipes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param upgradeRecipeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /api/{universe}/upgrade-recipes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<UpgradeRecipeDto>> insertAllUpgradeRecipesWithHttpInfo(@Param("universe") String universe, List<UpgradeRecipeDto> upgradeRecipeDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param upgradeRecipeDto  (required)
   * @return UpgradeRecipeDto
   */
  @RequestLine("PUT /api/{universe}/upgrade-recipes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  UpgradeRecipeDto updateUpgradeRecipe(@Param("universe") String universe, @Param("id") String id, UpgradeRecipeDto upgradeRecipeDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateUpgradeRecipe</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param upgradeRecipeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /api/{universe}/upgrade-recipes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<UpgradeRecipeDto> updateUpgradeRecipeWithHttpInfo(@Param("universe") String universe, @Param("id") String id, UpgradeRecipeDto upgradeRecipeDto);


}

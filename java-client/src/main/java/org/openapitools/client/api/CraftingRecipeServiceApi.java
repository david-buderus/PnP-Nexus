package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.CraftingRecipeDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface CraftingRecipeServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/crafting-recipes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllCraftingRecipes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllCraftingRecipes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/crafting-recipes?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllCraftingRecipesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllCraftingRecipes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllCraftingRecipesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /{universe}/crafting-recipes?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllCraftingRecipes(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllCraftingRecipesQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllCraftingRecipes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /{universe}/crafting-recipes?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllCraftingRecipesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllCraftingRecipesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllCraftingRecipes</code> method in a fluent style.
   */
  public static class DeleteAllCraftingRecipesQueryParams extends HashMap<String, Object> {
    public DeleteAllCraftingRecipesQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /{universe}/crafting-recipes/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteCraftingRecipe(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteCraftingRecipe</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /{universe}/crafting-recipes/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteCraftingRecipeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;CraftingRecipeDto&gt;
   */
  @RequestLine("GET /{universe}/crafting-recipes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<CraftingRecipeDto> getAllCraftingRecipes(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllCraftingRecipes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/crafting-recipes?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<CraftingRecipeDto>> getAllCraftingRecipesWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllCraftingRecipes</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllCraftingRecipesQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;CraftingRecipeDto&gt;
   */
  @RequestLine("GET /{universe}/crafting-recipes?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<CraftingRecipeDto> getAllCraftingRecipes(@Param("universe") String universe, @QueryMap(encoded=true) GetAllCraftingRecipesQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllCraftingRecipes</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;CraftingRecipeDto&gt;
      */
      @RequestLine("GET /{universe}/crafting-recipes?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<CraftingRecipeDto>> getAllCraftingRecipesWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllCraftingRecipesQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllCraftingRecipes</code> method in a fluent style.
   */
  public static class GetAllCraftingRecipesQueryParams extends HashMap<String, Object> {
    public GetAllCraftingRecipesQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return CraftingRecipeDto
   */
  @RequestLine("GET /{universe}/crafting-recipes/{id}")
  @Headers({
    "Accept: */*",
  })
  CraftingRecipeDto getCraftingRecipe(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getCraftingRecipe</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/crafting-recipes/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<CraftingRecipeDto> getCraftingRecipeWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param craftingRecipeDto  (required)
   * @return List&lt;CraftingRecipeDto&gt;
   */
  @RequestLine("POST /{universe}/crafting-recipes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<CraftingRecipeDto> insertAllCraftingRecipes(@Param("universe") String universe, List<CraftingRecipeDto> craftingRecipeDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllCraftingRecipes</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param craftingRecipeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /{universe}/crafting-recipes")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<CraftingRecipeDto>> insertAllCraftingRecipesWithHttpInfo(@Param("universe") String universe, List<CraftingRecipeDto> craftingRecipeDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param craftingRecipeDto  (required)
   * @return CraftingRecipeDto
   */
  @RequestLine("PUT /{universe}/crafting-recipes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  CraftingRecipeDto updateCraftingRecipe(@Param("universe") String universe, @Param("id") String id, CraftingRecipeDto craftingRecipeDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateCraftingRecipe</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param craftingRecipeDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /{universe}/crafting-recipes/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<CraftingRecipeDto> updateCraftingRecipeWithHttpInfo(@Param("universe") String universe, @Param("id") String id, CraftingRecipeDto craftingRecipeDto);


}

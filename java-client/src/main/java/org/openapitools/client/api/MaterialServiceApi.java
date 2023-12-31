package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.MaterialDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface MaterialServiceApi extends ApiClient.Api {


  /**
   * Deletes all objects with the given ids from the database
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/materials?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  void deleteAllMaterials(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Deletes all objects with the given ids from the database
   * Similar to <code>deleteAllMaterials</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (required)
   */
  @RequestLine("DELETE /{universe}/materials?ids={ids}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteAllMaterialsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Deletes all objects with the given ids from the database
   * 
   * Note, this is equivalent to the other <code>deleteAllMaterials</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link DeleteAllMaterialsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (required)</li>
   *   </ul>
   */
  @RequestLine("DELETE /{universe}/materials?ids={ids}")
  @Headers({
  "Accept: application/json",
  })
  void deleteAllMaterials(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllMaterialsQueryParams queryParams);

  /**
  * Deletes all objects with the given ids from the database
  * 
  * Note, this is equivalent to the other <code>deleteAllMaterials</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (required)</li>
      *   </ul>
      */
      @RequestLine("DELETE /{universe}/materials?ids={ids}")
      @Headers({
    "Accept: application/json",
      })
   ApiResponse<Void> deleteAllMaterialsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) DeleteAllMaterialsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>deleteAllMaterials</code> method in a fluent style.
   */
  public static class DeleteAllMaterialsQueryParams extends HashMap<String, Object> {
    public DeleteAllMaterialsQueryParams ids(final List<String> value) {
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
  @RequestLine("DELETE /{universe}/materials/{id}")
  @Headers({
    "Accept: application/json",
  })
  void deleteMaterial(@Param("universe") String universe, @Param("id") String id);

  /**
   * Deletes an object from the database
   * Similar to <code>deleteMaterial</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   */
  @RequestLine("DELETE /{universe}/materials/{id}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> deleteMaterialWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Get all objects from the database
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return List&lt;MaterialDto&gt;
   */
  @RequestLine("GET /{universe}/materials?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  List<MaterialDto> getAllMaterials(@Param("universe") String universe, @Param("ids") List<String> ids);

  /**
   * Get all objects from the database
   * Similar to <code>getAllMaterials</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param ids  (optional)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/materials?ids={ids}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<MaterialDto>> getAllMaterialsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


  /**
   * Get all objects from the database
   * 
   * Note, this is equivalent to the other <code>getAllMaterials</code> method,
   * but with the query parameters collected into a single Map parameter. This
   * is convenient for services with optional query parameters, especially when
   * used with the {@link GetAllMaterialsQueryParams} class that allows for
   * building up this map in a fluent style.
   * @param universe  (required)
   * @param queryParams Map of query parameters as name-value pairs
   *   <p>The following elements may be specified in the query map:</p>
   *   <ul>
   *   <li>ids -  (optional)</li>
   *   </ul>
   * @return List&lt;MaterialDto&gt;
   */
  @RequestLine("GET /{universe}/materials?ids={ids}")
  @Headers({
  "Accept: */*",
  })
  List<MaterialDto> getAllMaterials(@Param("universe") String universe, @QueryMap(encoded=true) GetAllMaterialsQueryParams queryParams);

  /**
  * Get all objects from the database
  * 
  * Note, this is equivalent to the other <code>getAllMaterials</code> that receives the query parameters as a map,
  * but this one also exposes the Http response headers
              * @param universe  (required)
      * @param queryParams Map of query parameters as name-value pairs
      *   <p>The following elements may be specified in the query map:</p>
      *   <ul>
          *   <li>ids -  (optional)</li>
      *   </ul>
          * @return List&lt;MaterialDto&gt;
      */
      @RequestLine("GET /{universe}/materials?ids={ids}")
      @Headers({
    "Accept: */*",
      })
   ApiResponse<List<MaterialDto>> getAllMaterialsWithHttpInfo(@Param("universe") String universe, @QueryMap(encoded=true) GetAllMaterialsQueryParams queryParams);


   /**
   * A convenience class for generating query parameters for the
   * <code>getAllMaterials</code> method in a fluent style.
   */
  public static class GetAllMaterialsQueryParams extends HashMap<String, Object> {
    public GetAllMaterialsQueryParams ids(final List<String> value) {
      put("ids", EncodingUtils.encodeCollection(value, "multi"));
      return this;
    }
  }

  /**
   * Get an object from the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return MaterialDto
   */
  @RequestLine("GET /{universe}/materials/{id}")
  @Headers({
    "Accept: */*",
  })
  MaterialDto getMaterial(@Param("universe") String universe, @Param("id") String id);

  /**
   * Get an object from the database
   * Similar to <code>getMaterial</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{universe}/materials/{id}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<MaterialDto> getMaterialWithHttpInfo(@Param("universe") String universe, @Param("id") String id);



  /**
   * Inserts the objects into the database
   * 
   * @param universe  (required)
   * @param materialDto  (required)
   * @return List&lt;MaterialDto&gt;
   */
  @RequestLine("POST /{universe}/materials")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  List<MaterialDto> insertAllMaterials(@Param("universe") String universe, List<MaterialDto> materialDto);

  /**
   * Inserts the objects into the database
   * Similar to <code>insertAllMaterials</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param materialDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("POST /{universe}/materials")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<List<MaterialDto>> insertAllMaterialsWithHttpInfo(@Param("universe") String universe, List<MaterialDto> materialDto);



  /**
   * Updates an object in the database
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param materialDto  (required)
   * @return MaterialDto
   */
  @RequestLine("PUT /{universe}/materials/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  MaterialDto updateMaterial(@Param("universe") String universe, @Param("id") String id, MaterialDto materialDto);

  /**
   * Updates an object in the database
   * Similar to <code>updateMaterial</code> but it also returns the http response headers .
   * 
   * @param universe  (required)
   * @param id  (required)
   * @param materialDto  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("PUT /{universe}/materials/{id}")
  @Headers({
    "Content-Type: application/json",
    "Accept: */*",
  })
  ApiResponse<MaterialDto> updateMaterialWithHttpInfo(@Param("universe") String universe, @Param("id") String id, MaterialDto materialDto);


}

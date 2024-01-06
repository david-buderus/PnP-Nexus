package org.openapitools.client.api;

import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;
import java.util.HashMap;
import java.util.List;
import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;
import org.openapitools.client.model.GetAllItems200ResponseInnerDto;
import org.openapitools.client.model.ItemDto;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface ItemServiceApi extends ApiClient.Api {


    /**
     * Deletes all objects with the given ids from the database
     *
     * @param universe (required)
     * @param ids      (required)
     */
    @RequestLine("DELETE /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: application/json",
    })
    void deleteAllItems(@Param("universe") String universe, @Param("ids") List<String> ids);

    /**
     * Deletes all objects with the given ids from the database Similar to <code>deleteAllItems</code> but it also
     * returns the http response headers .
     *
     * @param universe (required)
     * @param ids      (required)
     */
    @RequestLine("DELETE /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: application/json",
    })
    ApiResponse<Void> deleteAllItemsWithHttpInfo(@Param("universe") String universe, @Param("ids") List<String> ids);


    /**
     * Deletes all objects with the given ids from the database
     * <p>
     * Note, this is equivalent to the other <code>deleteAllItems</code> method, but with the query parameters collected
     * into a single Map parameter. This is convenient for services with optional query parameters, especially when used
     * with the {@link DeleteAllItemsQueryParams} class that allows for building up this map in a fluent style.
     *
     * @param universe    (required)
     * @param queryParams Map of query parameters as name-value pairs
     *                    <p>The following elements may be specified in the query map:</p>
     *                    <ul>
     *                    <li>ids -  (required)</li>
     *                    </ul>
     */
    @RequestLine("DELETE /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: application/json",
    })
    void deleteAllItems(@Param("universe") String universe,
        @QueryMap(encoded = true) DeleteAllItemsQueryParams queryParams);

    /**
     * Deletes all objects with the given ids from the database
     * <p>
     * Note, this is equivalent to the other <code>deleteAllItems</code> that receives the query parameters as a map,
     * but this one also exposes the Http response headers
     *
     * @param universe    (required)
     * @param queryParams Map of query parameters as name-value pairs
     *                    <p>The following elements may be specified in the query map:</p>
     *                    <ul>
     *                    <li>ids -  (required)</li>
     *                    </ul>
     */
    @RequestLine("DELETE /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: application/json",
    })
    ApiResponse<Void> deleteAllItemsWithHttpInfo(@Param("universe") String universe,
        @QueryMap(encoded = true) DeleteAllItemsQueryParams queryParams);


    /**
     * A convenience class for generating query parameters for the
     * <code>deleteAllItems</code> method in a fluent style.
     */
    public static class DeleteAllItemsQueryParams extends HashMap<String, Object> {

        public DeleteAllItemsQueryParams ids(final List<String> value) {
            put("ids", EncodingUtils.encodeCollection(value, "multi"));
            return this;
        }
    }

    /**
     * Deletes an object from the database
     *
     * @param universe (required)
     * @param id       (required)
     */
    @RequestLine("DELETE /api/{universe}/items/{id}")
    @Headers({
        "Accept: application/json",
    })
    void deleteItem(@Param("universe") String universe, @Param("id") String id);

    /**
     * Deletes an object from the database Similar to <code>deleteItem</code> but it also returns the http response
     * headers .
     *
     * @param universe (required)
     * @param id       (required)
     */
    @RequestLine("DELETE /api/{universe}/items/{id}")
    @Headers({
        "Accept: application/json",
    })
    ApiResponse<Void> deleteItemWithHttpInfo(@Param("universe") String universe, @Param("id") String id);


    /**
     * Get all objects from the database
     *
     * @param universe (required)
     * @param ids      (optional)
     * @return List&lt;GetAllItems200ResponseInnerDto&gt;
     */
    @RequestLine("GET /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: */*",
    })
    List<GetAllItems200ResponseInnerDto> getAllItems(@Param("universe") String universe,
        @Param("ids") List<String> ids);

    /**
     * Get all objects from the database Similar to <code>getAllItems</code> but it also returns the http response
     * headers .
     *
     * @param universe (required)
     * @param ids      (optional)
     * @return A ApiResponse that wraps the response boyd and the http headers.
     */
    @RequestLine("GET /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: */*",
    })
    ApiResponse<List<GetAllItems200ResponseInnerDto>> getAllItemsWithHttpInfo(@Param("universe") String universe,
        @Param("ids") List<String> ids);


    /**
     * Get all objects from the database
     * <p>
     * Note, this is equivalent to the other <code>getAllItems</code> method, but with the query parameters collected
     * into a single Map parameter. This is convenient for services with optional query parameters, especially when used
     * with the {@link GetAllItemsQueryParams} class that allows for building up this map in a fluent style.
     *
     * @param universe    (required)
     * @param queryParams Map of query parameters as name-value pairs
     *                    <p>The following elements may be specified in the query map:</p>
     *                    <ul>
     *                    <li>ids -  (optional)</li>
     *                    </ul>
     * @return List&lt;GetAllItems200ResponseInnerDto&gt;
     */
    @RequestLine("GET /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: */*",
    })
    List<GetAllItems200ResponseInnerDto> getAllItems(@Param("universe") String universe,
        @QueryMap(encoded = true) GetAllItemsQueryParams queryParams);

    /**
     * Get all objects from the database
     * <p>
     * Note, this is equivalent to the other <code>getAllItems</code> that receives the query parameters as a map, but
     * this one also exposes the Http response headers
     *
     * @param universe    (required)
     * @param queryParams Map of query parameters as name-value pairs
     *                    <p>The following elements may be specified in the query map:</p>
     *                    <ul>
     *                    <li>ids -  (optional)</li>
     *                    </ul>
     * @return List&lt;GetAllItems200ResponseInnerDto&gt;
     */
    @RequestLine("GET /api/{universe}/items?ids={ids}")
    @Headers({
        "Accept: */*",
    })
    ApiResponse<List<GetAllItems200ResponseInnerDto>> getAllItemsWithHttpInfo(@Param("universe") String universe,
        @QueryMap(encoded = true) GetAllItemsQueryParams queryParams);


    /**
     * A convenience class for generating query parameters for the
     * <code>getAllItems</code> method in a fluent style.
     */
    public static class GetAllItemsQueryParams extends HashMap<String, Object> {

        public GetAllItemsQueryParams ids(final List<String> value) {
            put("ids", EncodingUtils.encodeCollection(value, "multi"));
            return this;
        }
    }

    /**
     * Get an object from the database
     *
     * @param universe (required)
     * @param id       (required)
     * @return GetAllItems200ResponseInnerDto
     */
    @RequestLine("GET /api/{universe}/items/{id}")
    @Headers({
        "Accept: */*",
    })
    GetAllItems200ResponseInnerDto getItem(@Param("universe") String universe, @Param("id") String id);

    /**
     * Get an object from the database Similar to <code>getItem</code> but it also returns the http response headers .
     *
     * @param universe (required)
     * @param id       (required)
     * @return A ApiResponse that wraps the response boyd and the http headers.
     */
    @RequestLine("GET /api/{universe}/items/{id}")
    @Headers({
        "Accept: */*",
    })
    ApiResponse<GetAllItems200ResponseInnerDto> getItemWithHttpInfo(@Param("universe") String universe,
        @Param("id") String id);


    /**
     * Inserts the objects into the database
     *
     * @param universe                       (required)
     * @param getAllItems200ResponseInnerDto (required)
     * @return List&lt;GetAllItems200ResponseInnerDto&gt;
     */
    @RequestLine("POST /api/{universe}/items")
    @Headers({
        "Content-Type: application/json",
        "Accept: */*",
    })
    List<ItemDto> insertAllItems(@Param("universe") String universe, List<ItemDto> getAllItems200ResponseInnerDto);

    /**
     * Inserts the objects into the database Similar to <code>insertAllItems</code> but it also returns the http
     * response headers .
     *
     * @param universe                       (required)
     * @param getAllItems200ResponseInnerDto (required)
     * @return A ApiResponse that wraps the response boyd and the http headers.
     */
    @RequestLine("POST /api/{universe}/items")
    @Headers({
        "Content-Type: application/json",
        "Accept: */*",
    })
    ApiResponse<List<GetAllItems200ResponseInnerDto>> insertAllItemsWithHttpInfo(@Param("universe") String universe,
        List<GetAllItems200ResponseInnerDto> getAllItems200ResponseInnerDto);


    /**
     * Updates an object in the database
     *
     * @param universe                       (required)
     * @param id                             (required)
     * @param getAllItems200ResponseInnerDto (required)
     * @return GetAllItems200ResponseInnerDto
     */
    @RequestLine("PUT /api/{universe}/items/{id}")
    @Headers({
        "Content-Type: application/json",
        "Accept: */*",
    })
    GetAllItems200ResponseInnerDto updateItem(@Param("universe") String universe, @Param("id") String id,
        GetAllItems200ResponseInnerDto getAllItems200ResponseInnerDto);

    /**
     * Updates an object in the database Similar to <code>updateItem</code> but it also returns the http response
     * headers .
     *
     * @param universe                       (required)
     * @param id                             (required)
     * @param getAllItems200ResponseInnerDto (required)
     * @return A ApiResponse that wraps the response boyd and the http headers.
     */
    @RequestLine("PUT /api/{universe}/items/{id}")
    @Headers({
        "Content-Type: application/json",
        "Accept: */*",
    })
    ApiResponse<GetAllItems200ResponseInnerDto> updateItemWithHttpInfo(@Param("universe") String universe,
        @Param("id") String id, GetAllItems200ResponseInnerDto getAllItems200ResponseInnerDto);


}

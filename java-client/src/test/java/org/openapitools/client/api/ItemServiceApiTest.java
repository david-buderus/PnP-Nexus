package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.GetAllItems200ResponseInnerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ItemServiceApi
 */
class ItemServiceApiTest {

    private ItemServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(ItemServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllItemsTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllItems(universe, ids);

        // TODO: test validations
    }

    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     *
     * This tests the overload of the method that uses a Map for query parameters instead of
     * listing them out individually.
     */
    @Test
    void deleteAllItemsTestQueryMap() {
        String universe = null;
        ItemServiceApi.DeleteAllItemsQueryParams queryParams = new ItemServiceApi.DeleteAllItemsQueryParams()
            .ids(null);
        // api.deleteAllItems(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteItemTest() {
        String universe = null;
        String id = null;
        // api.deleteItem(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllItemsTest() {
        String universe = null;
        List<String> ids = null;
        // List<GetAllItems200ResponseInnerDto> response = api.getAllItems(universe, ids);

        // TODO: test validations
    }

    /**
     * Get all objects from the database
     *
     * 
     *
     * This tests the overload of the method that uses a Map for query parameters instead of
     * listing them out individually.
     */
    @Test
    void getAllItemsTestQueryMap() {
        String universe = null;
        ItemServiceApi.GetAllItemsQueryParams queryParams = new ItemServiceApi.GetAllItemsQueryParams()
            .ids(null);
        // List<GetAllItems200ResponseInnerDto> response = api.getAllItems(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getItemTest() {
        String universe = null;
        String id = null;
        // GetAllItems200ResponseInnerDto response = api.getItem(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllItemsTest() {
        String universe = null;
        List<GetAllItems200ResponseInnerDto> getAllItems200ResponseInnerDto = null;
        // List<GetAllItems200ResponseInnerDto> response = api.insertAllItems(universe, getAllItems200ResponseInnerDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateItemTest() {
        String universe = null;
        String id = null;
        GetAllItems200ResponseInnerDto getAllItems200ResponseInnerDto = null;
        // GetAllItems200ResponseInnerDto response = api.updateItem(universe, id, getAllItems200ResponseInnerDto);

        // TODO: test validations
    }

    
}

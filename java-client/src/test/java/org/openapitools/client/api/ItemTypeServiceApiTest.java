package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.ItemTypeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ItemTypeServiceApi
 */
class ItemTypeServiceApiTest {

    private ItemTypeServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(ItemTypeServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllItemTypesTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllItemTypes(universe, ids);

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
    void deleteAllItemTypesTestQueryMap() {
        String universe = null;
        ItemTypeServiceApi.DeleteAllItemTypesQueryParams queryParams = new ItemTypeServiceApi.DeleteAllItemTypesQueryParams()
            .ids(null);
        // api.deleteAllItemTypes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteItemTypeTest() {
        String universe = null;
        String id = null;
        // api.deleteItemType(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllItemTypesTest() {
        String universe = null;
        List<String> ids = null;
        // List<ItemTypeDto> response = api.getAllItemTypes(universe, ids);

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
    void getAllItemTypesTestQueryMap() {
        String universe = null;
        ItemTypeServiceApi.GetAllItemTypesQueryParams queryParams = new ItemTypeServiceApi.GetAllItemTypesQueryParams()
            .ids(null);
        // List<ItemTypeDto> response = api.getAllItemTypes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getItemTypeTest() {
        String universe = null;
        String id = null;
        // ItemTypeDto response = api.getItemType(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllItemTypesTest() {
        String universe = null;
        List<ItemTypeDto> itemTypeDto = null;
        // List<ItemTypeDto> response = api.insertAllItemTypes(universe, itemTypeDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateItemTypeTest() {
        String universe = null;
        String id = null;
        ItemTypeDto itemTypeDto = null;
        // ItemTypeDto response = api.updateItemType(universe, id, itemTypeDto);

        // TODO: test validations
    }

    
}

package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.ItemTypeTranslationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ItemTypeTranslationServiceApi
 */
class ItemTypeTranslationServiceApiTest {

    private ItemTypeTranslationServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(ItemTypeTranslationServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllItemTypeTranslationsTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllItemTypeTranslations(universe, ids);

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
    void deleteAllItemTypeTranslationsTestQueryMap() {
        String universe = null;
        ItemTypeTranslationServiceApi.DeleteAllItemTypeTranslationsQueryParams queryParams = new ItemTypeTranslationServiceApi.DeleteAllItemTypeTranslationsQueryParams()
            .ids(null);
        // api.deleteAllItemTypeTranslations(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteItemTypeTranslationTest() {
        String universe = null;
        String id = null;
        // api.deleteItemTypeTranslation(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllItemTypeTranslationsTest() {
        String universe = null;
        List<String> ids = null;
        // List<ItemTypeTranslationDto> response = api.getAllItemTypeTranslations(universe, ids);

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
    void getAllItemTypeTranslationsTestQueryMap() {
        String universe = null;
        ItemTypeTranslationServiceApi.GetAllItemTypeTranslationsQueryParams queryParams = new ItemTypeTranslationServiceApi.GetAllItemTypeTranslationsQueryParams()
            .ids(null);
        // List<ItemTypeTranslationDto> response = api.getAllItemTypeTranslations(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getItemTypeTranslationTest() {
        String universe = null;
        String id = null;
        // ItemTypeTranslationDto response = api.getItemTypeTranslation(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllItemTypeTranslationsTest() {
        String universe = null;
        List<ItemTypeTranslationDto> itemTypeTranslationDto = null;
        // List<ItemTypeTranslationDto> response = api.insertAllItemTypeTranslations(universe, itemTypeTranslationDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateItemTypeTranslationTest() {
        String universe = null;
        String id = null;
        ItemTypeTranslationDto itemTypeTranslationDto = null;
        // ItemTypeTranslationDto response = api.updateItemTypeTranslation(universe, id, itemTypeTranslationDto);

        // TODO: test validations
    }

    
}

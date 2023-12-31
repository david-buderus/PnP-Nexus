package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.SecondaryAttributeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for SecondaryAttributeServiceApi
 */
class SecondaryAttributeServiceApiTest {

    private SecondaryAttributeServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(SecondaryAttributeServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllSecondaryAttributesTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllSecondaryAttributes(universe, ids);

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
    void deleteAllSecondaryAttributesTestQueryMap() {
        String universe = null;
        SecondaryAttributeServiceApi.DeleteAllSecondaryAttributesQueryParams queryParams = new SecondaryAttributeServiceApi.DeleteAllSecondaryAttributesQueryParams()
            .ids(null);
        // api.deleteAllSecondaryAttributes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteSecondaryAttributeTest() {
        String universe = null;
        String id = null;
        // api.deleteSecondaryAttribute(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllSecondaryAttributesTest() {
        String universe = null;
        List<String> ids = null;
        // List<SecondaryAttributeDto> response = api.getAllSecondaryAttributes(universe, ids);

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
    void getAllSecondaryAttributesTestQueryMap() {
        String universe = null;
        SecondaryAttributeServiceApi.GetAllSecondaryAttributesQueryParams queryParams = new SecondaryAttributeServiceApi.GetAllSecondaryAttributesQueryParams()
            .ids(null);
        // List<SecondaryAttributeDto> response = api.getAllSecondaryAttributes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getSecondaryAttributeTest() {
        String universe = null;
        String id = null;
        // SecondaryAttributeDto response = api.getSecondaryAttribute(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllSecondaryAttributesTest() {
        String universe = null;
        List<SecondaryAttributeDto> secondaryAttributeDto = null;
        // List<SecondaryAttributeDto> response = api.insertAllSecondaryAttributes(universe, secondaryAttributeDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateSecondaryAttributeTest() {
        String universe = null;
        String id = null;
        SecondaryAttributeDto secondaryAttributeDto = null;
        // SecondaryAttributeDto response = api.updateSecondaryAttribute(universe, id, secondaryAttributeDto);

        // TODO: test validations
    }

    
}

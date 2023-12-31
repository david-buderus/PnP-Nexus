package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.PrimaryAttributeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for PrimaryAttributeServiceApi
 */
class PrimaryAttributeServiceApiTest {

    private PrimaryAttributeServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(PrimaryAttributeServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllPrimaryAttributesTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllPrimaryAttributes(universe, ids);

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
    void deleteAllPrimaryAttributesTestQueryMap() {
        String universe = null;
        PrimaryAttributeServiceApi.DeleteAllPrimaryAttributesQueryParams queryParams = new PrimaryAttributeServiceApi.DeleteAllPrimaryAttributesQueryParams()
            .ids(null);
        // api.deleteAllPrimaryAttributes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deletePrimaryAttributeTest() {
        String universe = null;
        String id = null;
        // api.deletePrimaryAttribute(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllPrimaryAttributesTest() {
        String universe = null;
        List<String> ids = null;
        // List<PrimaryAttributeDto> response = api.getAllPrimaryAttributes(universe, ids);

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
    void getAllPrimaryAttributesTestQueryMap() {
        String universe = null;
        PrimaryAttributeServiceApi.GetAllPrimaryAttributesQueryParams queryParams = new PrimaryAttributeServiceApi.GetAllPrimaryAttributesQueryParams()
            .ids(null);
        // List<PrimaryAttributeDto> response = api.getAllPrimaryAttributes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getPrimaryAttributeTest() {
        String universe = null;
        String id = null;
        // PrimaryAttributeDto response = api.getPrimaryAttribute(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllPrimaryAttributesTest() {
        String universe = null;
        List<PrimaryAttributeDto> primaryAttributeDto = null;
        // List<PrimaryAttributeDto> response = api.insertAllPrimaryAttributes(universe, primaryAttributeDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updatePrimaryAttributeTest() {
        String universe = null;
        String id = null;
        PrimaryAttributeDto primaryAttributeDto = null;
        // PrimaryAttributeDto response = api.updatePrimaryAttribute(universe, id, primaryAttributeDto);

        // TODO: test validations
    }

    
}

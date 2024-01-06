package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.TalentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for TalentServiceApi
 */
class TalentServiceApiTest {

    private TalentServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(TalentServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllTalentsTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllTalents(universe, ids);

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
    void deleteAllTalentsTestQueryMap() {
        String universe = null;
        TalentServiceApi.DeleteAllTalentsQueryParams queryParams = new TalentServiceApi.DeleteAllTalentsQueryParams()
            .ids(null);
        // api.deleteAllTalents(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteTalentTest() {
        String universe = null;
        String id = null;
        // api.deleteTalent(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllTalentsTest() {
        String universe = null;
        List<String> ids = null;
        // List<TalentDto> response = api.getAllTalents(universe, ids);

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
    void getAllTalentsTestQueryMap() {
        String universe = null;
        TalentServiceApi.GetAllTalentsQueryParams queryParams = new TalentServiceApi.GetAllTalentsQueryParams()
            .ids(null);
        // List<TalentDto> response = api.getAllTalents(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getTalentTest() {
        String universe = null;
        String id = null;
        // TalentDto response = api.getTalent(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllTalentsTest() {
        String universe = null;
        List<TalentDto> talentDto = null;
        // List<TalentDto> response = api.insertAllTalents(universe, talentDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateTalentTest() {
        String universe = null;
        String id = null;
        TalentDto talentDto = null;
        // TalentDto response = api.updateTalent(universe, id, talentDto);

        // TODO: test validations
    }

    
}

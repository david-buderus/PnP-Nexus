package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.SpellDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for SpellServiceApi
 */
class SpellServiceApiTest {

    private SpellServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(SpellServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllSpellsTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllSpells(universe, ids);

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
    void deleteAllSpellsTestQueryMap() {
        String universe = null;
        SpellServiceApi.DeleteAllSpellsQueryParams queryParams = new SpellServiceApi.DeleteAllSpellsQueryParams()
            .ids(null);
        // api.deleteAllSpells(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteSpellTest() {
        String universe = null;
        String id = null;
        // api.deleteSpell(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllSpellsTest() {
        String universe = null;
        List<String> ids = null;
        // List<SpellDto> response = api.getAllSpells(universe, ids);

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
    void getAllSpellsTestQueryMap() {
        String universe = null;
        SpellServiceApi.GetAllSpellsQueryParams queryParams = new SpellServiceApi.GetAllSpellsQueryParams()
            .ids(null);
        // List<SpellDto> response = api.getAllSpells(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getSpellTest() {
        String universe = null;
        String id = null;
        // SpellDto response = api.getSpell(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllSpellsTest() {
        String universe = null;
        List<SpellDto> spellDto = null;
        // List<SpellDto> response = api.insertAllSpells(universe, spellDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateSpellTest() {
        String universe = null;
        String id = null;
        SpellDto spellDto = null;
        // SpellDto response = api.updateSpell(universe, id, spellDto);

        // TODO: test validations
    }

    
}

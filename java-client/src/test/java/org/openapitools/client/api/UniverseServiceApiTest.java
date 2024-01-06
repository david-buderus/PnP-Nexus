package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.UniverseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UniverseServiceApi
 */
class UniverseServiceApiTest {

    private UniverseServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(UniverseServiceApi.class);
    }

    
    /**
     * Add the given access right to the given user
     *
     * 
     */
    @Test
    void addPermissionTest() {
        String universe = null;
        String username = null;
        String accessPermission = null;
        // api.addPermission(universe, username, accessPermission);

        // TODO: test validations
    }

    /**
     * Add the given access right to the given user
     *
     * 
     *
     * This tests the overload of the method that uses a Map for query parameters instead of
     * listing them out individually.
     */
    @Test
    void addPermissionTestQueryMap() {
        String universe = null;
        UniverseServiceApi.AddPermissionQueryParams queryParams = new UniverseServiceApi.AddPermissionQueryParams()
            .username(null)
            .accessPermission(null);
        // api.addPermission(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Create a universe
     *
     * 
     */
    @Test
    void createUniverseTest() {
        UniverseDto universeDto = null;
        // UniverseDto response = api.createUniverse(universeDto);

        // TODO: test validations
    }

    
    /**
     * Delete a universe
     *
     * 
     */
    @Test
    void deleteUniverseTest() {
        String universe = null;
        // api.deleteUniverse(universe);

        // TODO: test validations
    }

    
    /**
     * Get all Universes
     *
     * 
     */
    @Test
    void getAllUniversesTest() {
        // List<UniverseDto> response = api.getAllUniverses();

        // TODO: test validations
    }

    
    /**
     * Get a universe
     *
     * 
     */
    @Test
    void getUniverseTest() {
        String universe = null;
        // UniverseDto response = api.getUniverse(universe);

        // TODO: test validations
    }

    
    /**
     * Removes all access rights to the universe from the given user
     *
     * 
     */
    @Test
    void removePermissionTest() {
        String universe = null;
        String username = null;
        // api.removePermission(universe, username);

        // TODO: test validations
    }

    /**
     * Removes all access rights to the universe from the given user
     *
     * 
     *
     * This tests the overload of the method that uses a Map for query parameters instead of
     * listing them out individually.
     */
    @Test
    void removePermissionTestQueryMap() {
        String universe = null;
        UniverseServiceApi.RemovePermissionQueryParams queryParams = new UniverseServiceApi.RemovePermissionQueryParams()
            .username(null);
        // api.removePermission(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Update a universe
     *
     * 
     */
    @Test
    void updateUniverseTest() {
        String universe = null;
        UniverseDto universeDto = null;
        // UniverseDto response = api.updateUniverse(universe, universeDto);

        // TODO: test validations
    }

    
}

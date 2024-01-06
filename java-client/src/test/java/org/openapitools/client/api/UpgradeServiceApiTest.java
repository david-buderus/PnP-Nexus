package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.UpgradeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UpgradeServiceApi
 */
class UpgradeServiceApiTest {

    private UpgradeServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(UpgradeServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllUpgradesTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllUpgrades(universe, ids);

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
    void deleteAllUpgradesTestQueryMap() {
        String universe = null;
        UpgradeServiceApi.DeleteAllUpgradesQueryParams queryParams = new UpgradeServiceApi.DeleteAllUpgradesQueryParams()
            .ids(null);
        // api.deleteAllUpgrades(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteUpgradeTest() {
        String universe = null;
        String id = null;
        // api.deleteUpgrade(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllUpgradesTest() {
        String universe = null;
        List<String> ids = null;
        // List<UpgradeDto> response = api.getAllUpgrades(universe, ids);

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
    void getAllUpgradesTestQueryMap() {
        String universe = null;
        UpgradeServiceApi.GetAllUpgradesQueryParams queryParams = new UpgradeServiceApi.GetAllUpgradesQueryParams()
            .ids(null);
        // List<UpgradeDto> response = api.getAllUpgrades(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getUpgradeTest() {
        String universe = null;
        String id = null;
        // UpgradeDto response = api.getUpgrade(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllUpgradesTest() {
        String universe = null;
        List<UpgradeDto> upgradeDto = null;
        // List<UpgradeDto> response = api.insertAllUpgrades(universe, upgradeDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateUpgradeTest() {
        String universe = null;
        String id = null;
        UpgradeDto upgradeDto = null;
        // UpgradeDto response = api.updateUpgrade(universe, id, upgradeDto);

        // TODO: test validations
    }

    
}

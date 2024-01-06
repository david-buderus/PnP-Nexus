package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.UpgradeRecipeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UpgradeRecipeServiceApi
 */
class UpgradeRecipeServiceApiTest {

    private UpgradeRecipeServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(UpgradeRecipeServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllUpgradeRecipesTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllUpgradeRecipes(universe, ids);

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
    void deleteAllUpgradeRecipesTestQueryMap() {
        String universe = null;
        UpgradeRecipeServiceApi.DeleteAllUpgradeRecipesQueryParams queryParams = new UpgradeRecipeServiceApi.DeleteAllUpgradeRecipesQueryParams()
            .ids(null);
        // api.deleteAllUpgradeRecipes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteUpgradeRecipeTest() {
        String universe = null;
        String id = null;
        // api.deleteUpgradeRecipe(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllUpgradeRecipesTest() {
        String universe = null;
        List<String> ids = null;
        // List<UpgradeRecipeDto> response = api.getAllUpgradeRecipes(universe, ids);

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
    void getAllUpgradeRecipesTestQueryMap() {
        String universe = null;
        UpgradeRecipeServiceApi.GetAllUpgradeRecipesQueryParams queryParams = new UpgradeRecipeServiceApi.GetAllUpgradeRecipesQueryParams()
            .ids(null);
        // List<UpgradeRecipeDto> response = api.getAllUpgradeRecipes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getUpgradeRecipeTest() {
        String universe = null;
        String id = null;
        // UpgradeRecipeDto response = api.getUpgradeRecipe(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllUpgradeRecipesTest() {
        String universe = null;
        List<UpgradeRecipeDto> upgradeRecipeDto = null;
        // List<UpgradeRecipeDto> response = api.insertAllUpgradeRecipes(universe, upgradeRecipeDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateUpgradeRecipeTest() {
        String universe = null;
        String id = null;
        UpgradeRecipeDto upgradeRecipeDto = null;
        // UpgradeRecipeDto response = api.updateUpgradeRecipe(universe, id, upgradeRecipeDto);

        // TODO: test validations
    }

    
}

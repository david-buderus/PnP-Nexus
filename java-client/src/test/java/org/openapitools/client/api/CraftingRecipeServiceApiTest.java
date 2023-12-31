package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.CraftingRecipeDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for CraftingRecipeServiceApi
 */
class CraftingRecipeServiceApiTest {

    private CraftingRecipeServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(CraftingRecipeServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllCraftingRecipesTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllCraftingRecipes(universe, ids);

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
    void deleteAllCraftingRecipesTestQueryMap() {
        String universe = null;
        CraftingRecipeServiceApi.DeleteAllCraftingRecipesQueryParams queryParams = new CraftingRecipeServiceApi.DeleteAllCraftingRecipesQueryParams()
            .ids(null);
        // api.deleteAllCraftingRecipes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteCraftingRecipeTest() {
        String universe = null;
        String id = null;
        // api.deleteCraftingRecipe(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllCraftingRecipesTest() {
        String universe = null;
        List<String> ids = null;
        // List<CraftingRecipeDto> response = api.getAllCraftingRecipes(universe, ids);

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
    void getAllCraftingRecipesTestQueryMap() {
        String universe = null;
        CraftingRecipeServiceApi.GetAllCraftingRecipesQueryParams queryParams = new CraftingRecipeServiceApi.GetAllCraftingRecipesQueryParams()
            .ids(null);
        // List<CraftingRecipeDto> response = api.getAllCraftingRecipes(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getCraftingRecipeTest() {
        String universe = null;
        String id = null;
        // CraftingRecipeDto response = api.getCraftingRecipe(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllCraftingRecipesTest() {
        String universe = null;
        List<CraftingRecipeDto> craftingRecipeDto = null;
        // List<CraftingRecipeDto> response = api.insertAllCraftingRecipes(universe, craftingRecipeDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateCraftingRecipeTest() {
        String universe = null;
        String id = null;
        CraftingRecipeDto craftingRecipeDto = null;
        // CraftingRecipeDto response = api.updateCraftingRecipe(universe, id, craftingRecipeDto);

        // TODO: test validations
    }

    
}

package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.MaterialDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for MaterialServiceApi
 */
class MaterialServiceApiTest {

    private MaterialServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(MaterialServiceApi.class);
    }

    
    /**
     * Deletes all objects with the given ids from the database
     *
     * 
     */
    @Test
    void deleteAllMaterialsTest() {
        String universe = null;
        List<String> ids = null;
        // api.deleteAllMaterials(universe, ids);

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
    void deleteAllMaterialsTestQueryMap() {
        String universe = null;
        MaterialServiceApi.DeleteAllMaterialsQueryParams queryParams = new MaterialServiceApi.DeleteAllMaterialsQueryParams()
            .ids(null);
        // api.deleteAllMaterials(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Deletes an object from the database
     *
     * 
     */
    @Test
    void deleteMaterialTest() {
        String universe = null;
        String id = null;
        // api.deleteMaterial(universe, id);

        // TODO: test validations
    }

    
    /**
     * Get all objects from the database
     *
     * 
     */
    @Test
    void getAllMaterialsTest() {
        String universe = null;
        List<String> ids = null;
        // List<MaterialDto> response = api.getAllMaterials(universe, ids);

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
    void getAllMaterialsTestQueryMap() {
        String universe = null;
        MaterialServiceApi.GetAllMaterialsQueryParams queryParams = new MaterialServiceApi.GetAllMaterialsQueryParams()
            .ids(null);
        // List<MaterialDto> response = api.getAllMaterials(universe, queryParams);

    // TODO: test validations
    }
    
    /**
     * Get an object from the database
     *
     * 
     */
    @Test
    void getMaterialTest() {
        String universe = null;
        String id = null;
        // MaterialDto response = api.getMaterial(universe, id);

        // TODO: test validations
    }

    
    /**
     * Inserts the objects into the database
     *
     * 
     */
    @Test
    void insertAllMaterialsTest() {
        String universe = null;
        List<MaterialDto> materialDto = null;
        // List<MaterialDto> response = api.insertAllMaterials(universe, materialDto);

        // TODO: test validations
    }

    
    /**
     * Updates an object in the database
     *
     * 
     */
    @Test
    void updateMaterialTest() {
        String universe = null;
        String id = null;
        MaterialDto materialDto = null;
        // MaterialDto response = api.updateMaterial(universe, id, materialDto);

        // TODO: test validations
    }

    
}

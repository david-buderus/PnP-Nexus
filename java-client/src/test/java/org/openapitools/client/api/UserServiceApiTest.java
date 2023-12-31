package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.model.GetPermissions200ResponseInnerDto;
import org.openapitools.client.model.PasswordChangeDto;
import org.openapitools.client.model.PnPUserCreationDto;
import org.openapitools.client.model.PnPUserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UserServiceApi
 */
class UserServiceApiTest {

    private UserServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(UserServiceApi.class);
    }

    
    /**
     * Create a user
     *
     * 
     */
    @Test
    void createUserTest() {
        PnPUserCreationDto pnPUserCreationDto = null;
        // api.createUser(pnPUserCreationDto);

        // TODO: test validations
    }

    
    /**
     * Gets the permissions of a user
     *
     * 
     */
    @Test
    void getPermissionsTest() {
        String username = null;
        // List<GetPermissions200ResponseInnerDto> response = api.getPermissions(username);

        // TODO: test validations
    }

    
    /**
     * Get a user
     *
     * 
     */
    @Test
    void getUserTest() {
        String username = null;
        // PnPUserDto response = api.getUser(username);

        // TODO: test validations
    }

    
    /**
     * Delete a user
     *
     * 
     */
    @Test
    void removeUserTest() {
        String username = null;
        // api.removeUser(username);

        // TODO: test validations
    }

    
    /**
     * Updates the password of a user
     *
     * 
     */
    @Test
    void updatePasswordTest() {
        String username = null;
        PasswordChangeDto passwordChangeDto = null;
        // api.updatePassword(username, passwordChangeDto);

        // TODO: test validations
    }

    
    /**
     * Updates the permissions of a user
     *
     * 
     */
    @Test
    void updatePermissionsTest() {
        String username = null;
        List<GetPermissions200ResponseInnerDto> getPermissions200ResponseInnerDto = null;
        // api.updatePermissions(username, getPermissions200ResponseInnerDto);

        // TODO: test validations
    }

    
    /**
     * Updates a user
     *
     * 
     */
    @Test
    void updateUserTest() {
        String username = null;
        PnPUserDto pnPUserDto = null;
        // api.updateUser(username, pnPUserDto);

        // TODO: test validations
    }

    
}

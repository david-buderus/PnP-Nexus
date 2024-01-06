package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for AuthenticationServiceApi
 */
class AuthenticationServiceApiTest {

    private AuthenticationServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(AuthenticationServiceApi.class);
    }

    
    /**
     * Returns the username of the authenticated user
     *
     * 
     */
    @Test
    void getUsernameTest() {
        // String response = api.getUsername();

        // TODO: test validations
    }

    
}

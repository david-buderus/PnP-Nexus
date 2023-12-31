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
 * API tests for PageServiceApi
 */
class PageServiceApiTest {

    private PageServiceApi api;

    @BeforeEach
    public void setup() {
        api = new ApiClient().buildClient(PageServiceApi.class);
    }

    
    /**
     * 
     *
     * 
     */
    @Test
    void getPageTest() {
        String destination = null;
        // String response = api.getPage(destination);

        // TODO: test validations
    }

    
}

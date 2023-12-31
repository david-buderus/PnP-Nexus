package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface AuthenticationServiceApi extends ApiClient.Api {


  /**
   * Returns the username of the authenticated user
   * 
   * @return String
   */
  @RequestLine("GET /api/authentication/current-user")
  @Headers({
    "Accept: */*",
  })
  String getUsername();

  /**
   * Returns the username of the authenticated user
   * Similar to <code>getUsername</code> but it also returns the http response headers .
   * 
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/authentication/current-user")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<String> getUsernameWithHttpInfo();


}

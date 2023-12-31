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
public interface PageServiceApi extends ApiClient.Api {


  /**
   * 
   * 
   * @param destination  (required)
   * @return String
   */
  @RequestLine("GET /{destination}")
  @Headers({
    "Accept: text/html",
  })
  String getPage(@Param("destination") String destination);

  /**
   * 
   * Similar to <code>getPage</code> but it also returns the http response headers .
   * 
   * @param destination  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /{destination}")
  @Headers({
    "Accept: text/html",
  })
  ApiResponse<String> getPageWithHttpInfo(@Param("destination") String destination);


}

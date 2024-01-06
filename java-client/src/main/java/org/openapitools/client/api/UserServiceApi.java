package org.openapitools.client.api;

import org.openapitools.client.ApiClient;
import org.openapitools.client.EncodingUtils;
import org.openapitools.client.model.ApiResponse;

import org.openapitools.client.model.GetPermissions200ResponseInnerDto;
import org.openapitools.client.model.PasswordChangeDto;
import org.openapitools.client.model.PnPUserCreationDto;
import org.openapitools.client.model.PnPUserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import feign.*;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2023-12-30T12:56:25.947710400+01:00[Europe/Berlin]")
public interface UserServiceApi extends ApiClient.Api {


  /**
   * Create a user
   * 
   * @param pnPUserCreationDto  (required)
   */
  @RequestLine("POST /api/users")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  void createUser(PnPUserCreationDto pnPUserCreationDto);

  /**
   * Create a user
   * Similar to <code>createUser</code> but it also returns the http response headers .
   * 
   * @param pnPUserCreationDto  (required)
   */
  @RequestLine("POST /api/users")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<Void> createUserWithHttpInfo(PnPUserCreationDto pnPUserCreationDto);



  /**
   * Gets the permissions of a user
   * 
   * @param username  (required)
   * @return List&lt;GetPermissions200ResponseInnerDto&gt;
   */
  @RequestLine("GET /api/users/{username}/permissions")
  @Headers({
    "Accept: */*",
  })
  List<GetPermissions200ResponseInnerDto> getPermissions(@Param("username") String username);

  /**
   * Gets the permissions of a user
   * Similar to <code>getPermissions</code> but it also returns the http response headers .
   * 
   * @param username  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/users/{username}/permissions")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<List<GetPermissions200ResponseInnerDto>> getPermissionsWithHttpInfo(@Param("username") String username);



  /**
   * Get a user
   * 
   * @param username  (required)
   * @return PnPUserDto
   */
  @RequestLine("GET /api/users/{username}")
  @Headers({
    "Accept: */*",
  })
  PnPUserDto getUser(@Param("username") String username);

  /**
   * Get a user
   * Similar to <code>getUser</code> but it also returns the http response headers .
   * 
   * @param username  (required)
   * @return A ApiResponse that wraps the response boyd and the http headers.
   */
  @RequestLine("GET /api/users/{username}")
  @Headers({
    "Accept: */*",
  })
  ApiResponse<PnPUserDto> getUserWithHttpInfo(@Param("username") String username);



  /**
   * Delete a user
   * 
   * @param username  (required)
   */
  @RequestLine("DELETE /api/users/{username}")
  @Headers({
    "Accept: application/json",
  })
  void removeUser(@Param("username") String username);

  /**
   * Delete a user
   * Similar to <code>removeUser</code> but it also returns the http response headers .
   * 
   * @param username  (required)
   */
  @RequestLine("DELETE /api/users/{username}")
  @Headers({
    "Accept: application/json",
  })
  ApiResponse<Void> removeUserWithHttpInfo(@Param("username") String username);



  /**
   * Updates the password of a user
   * 
   * @param username  (required)
   * @param passwordChangeDto  (required)
   */
  @RequestLine("POST /api/users/{username}/password")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  void updatePassword(@Param("username") String username, PasswordChangeDto passwordChangeDto);

  /**
   * Updates the password of a user
   * Similar to <code>updatePassword</code> but it also returns the http response headers .
   * 
   * @param username  (required)
   * @param passwordChangeDto  (required)
   */
  @RequestLine("POST /api/users/{username}/password")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<Void> updatePasswordWithHttpInfo(@Param("username") String username, PasswordChangeDto passwordChangeDto);



  /**
   * Updates the permissions of a user
   * 
   * @param username  (required)
   * @param getPermissions200ResponseInnerDto  (required)
   */
  @RequestLine("POST /api/users/{username}/permissions")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  void updatePermissions(@Param("username") String username, List<GetPermissions200ResponseInnerDto> getPermissions200ResponseInnerDto);

  /**
   * Updates the permissions of a user
   * Similar to <code>updatePermissions</code> but it also returns the http response headers .
   * 
   * @param username  (required)
   * @param getPermissions200ResponseInnerDto  (required)
   */
  @RequestLine("POST /api/users/{username}/permissions")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<Void> updatePermissionsWithHttpInfo(@Param("username") String username, List<GetPermissions200ResponseInnerDto> getPermissions200ResponseInnerDto);



  /**
   * Updates a user
   * 
   * @param username  (required)
   * @param pnPUserDto  (required)
   */
  @RequestLine("PUT /api/users/{username}")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  void updateUser(@Param("username") String username, PnPUserDto pnPUserDto);

  /**
   * Updates a user
   * Similar to <code>updateUser</code> but it also returns the http response headers .
   * 
   * @param username  (required)
   * @param pnPUserDto  (required)
   */
  @RequestLine("PUT /api/users/{username}")
  @Headers({
    "Content-Type: application/json",
    "Accept: application/json",
  })
  ApiResponse<Void> updateUserWithHttpInfo(@Param("username") String username, PnPUserDto pnPUserDto);


}

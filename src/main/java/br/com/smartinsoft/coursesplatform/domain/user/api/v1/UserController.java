package br.com.smartinsoft.coursesplatform.domain.user.api.v1;

import br.com.smartinsoft.coursesplatform.config.component.UserLoggedBean;
import br.com.smartinsoft.coursesplatform.config.response.ApiServiceResponse;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.TokenRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.TokenValidationRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserOwnerRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserUpdateRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.response.UserResponse;
import br.com.smartinsoft.coursesplatform.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.net.URI;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RestController
@RequestMapping("/v1/user")
public class UserController {
  private static final String URI_REPRESENT_USER = "/user/{externalId}";

  @Autowired
  private UserLoggedBean userLogged;

  @Autowired
  private UserService userService;

  @Operation(summary = "Create user owner API",
      description = "API that allows the registration of an owner user on the Course Platform",
      tags = { "API User" }
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}
      ),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping("/owner")
  public ResponseEntity<UserResponse> createOwner(@Valid @RequestBody UserOwnerRequest request,
      UriComponentsBuilder uriBuilder) {

    log.info("Init create owner user {}", request.getCpf());
    UserResponse response = userService.createOwner(request);

    URI uri = uriBuilder.path(URI_REPRESENT_USER)
        .buildAndExpand(response.getExternalId())
        .toUri();

    log.info("Finished create owner user {}", response);
    return ResponseEntity.created(uri).body(response);
  }

  @Operation(summary = "Create user API",
      description = "API that allows the registration of an user on the Course Platform",
      tags = { "API User" }
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}
      ),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping
  public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest request,
      UriComponentsBuilder uriBuilder) {
    log.info("Init create user {}", request.getCpf());
    UserResponse response = userService.create(request, Boolean.FALSE);
    URI uri = uriBuilder.path(URI_REPRESENT_USER)
        .buildAndExpand(response.getExternalId())
        .toUri();

    log.info("Finished create user {}", response);
    return ResponseEntity.created(uri).body(response);
  }

  @Operation( summary = "Update User API",
      description = "API that allows alteration of user registration in general on the Course Platform",
      tags = { "API User" }
  )
  @ApiResponses( value =  {
      @ApiResponse(responseCode = "200", description = "Success", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(hidden = true)))
  })
  @PutMapping("/{externalId}")
  public ResponseEntity<UserResponse> update(@PathVariable String externalId,
      @Valid @RequestBody UserUpdateRequest request) {
    log.info("Init update user {}", externalId);
    UserResponse response = userService.update(externalId, request);
    log.info("Finished update user {}", response);
    return ResponseEntity.ok(response);
  }

  @Operation( summary = "Send token to User API",
      description = "API that send a token to the email address",
      tags = { "API User" }
  )
  @ApiResponses( value =  {
      @ApiResponse(responseCode = "200", description = "Success", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping("/sendToken")
  public ResponseEntity<ApiServiceResponse> sendToken(@Valid @RequestBody TokenRequest request) {
    log.info("Init send token to the email {}", request.getEmail());
    ApiServiceResponse response = userService.sendToken(request);
    log.info("Finished send token to the email {}", response);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Validate Token User API",
      description = "API that allows validating the token sent to the email address",
      tags = { "API User" })
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ApiServiceResponse.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true)))})
  @PostMapping("/validateToken")
  public ResponseEntity<ApiServiceResponse> validateToken(@Valid @RequestBody TokenValidationRequest request) {

    log.info("Init validationToken {}", request);
    ApiServiceResponse response = userService.validateToken(request);
    log.info("Finished validationToken {}", response);
    return ResponseEntity.ok(response);
  }

}

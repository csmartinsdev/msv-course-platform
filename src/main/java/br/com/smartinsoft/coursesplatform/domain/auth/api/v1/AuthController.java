package br.com.smartinsoft.coursesplatform.domain.auth.api.v1;

import br.com.smartinsoft.coursesplatform.config.component.MessageProperty;
import br.com.smartinsoft.coursesplatform.config.service.JwtTokenService;
import br.com.smartinsoft.coursesplatform.domain.auth.api.v1.request.AuthRequest;
import br.com.smartinsoft.coursesplatform.domain.auth.api.v1.response.AuthResponse;
import br.com.smartinsoft.coursesplatform.domain.auth.service.impl.AuthServiceImpl;
import br.com.smartinsoft.coursesplatform.domain.refreshtoken.entity.RefreshToken;
import br.com.smartinsoft.coursesplatform.domain.refreshtoken.service.RefreshTokenService;
import br.com.smartinsoft.coursesplatform.exception.RefreshTokenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {
  @Autowired
  private MessageProperty messageProperty;

  @Autowired
  private JwtTokenService jwtTokenService;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  private AuthServiceImpl authService;


  @Operation(
      summary = "Authenticate API",
      description = "API used to authenticate user in Courses Platform",
      tags = { "API Auth" }
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))}),
      @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(hidden = true))),
      @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(hidden = true)))
  })
  @PostMapping
  public ResponseEntity<AuthResponse> authenticate(final HttpServletRequest request, @Valid @RequestBody AuthRequest authRequest) {
    log.info("Init authenticate {}", authRequest.getLogin());
    AuthResponse authResponse = authService.auth(request, authRequest);
    log.info("Finished authenticate");
    return ResponseEntity.ok(authResponse);
  }

  @Operation(
      summary = "Refresh Token API",
      description = "API used to generate refresh token through to user authentication in Courses Platform",
      tags = { "API Auth" }
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Success", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class))}),
      @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(hidden = true))),
  })
  @GetMapping("/{refreshToken}/accessToken")
  public ResponseEntity<AuthResponse> refreshToken(@PathVariable String refreshToken) {
    log.info("Init refreshToken {}", refreshToken);

    return refreshTokenService.findByToken(refreshToken)
        .map(refreshTokenService::checkExpiration)
        .map(RefreshToken::getUser)
        .map(user -> ResponseEntity.ok(new AuthResponse(jwtTokenService.generateToken(user), refreshToken))
        ).orElseThrow(() -> new RefreshTokenException(messageProperty.getProperty("error.refreshToken.expired")));
  }


}

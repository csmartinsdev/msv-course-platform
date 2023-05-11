package br.com.smartinsoft.coursesplatform.domain.user.api.v1.request;

import br.com.smartinsoft.coursesplatform.config.validators.EmailValidation;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenValidationRequest {
  @NotEmpty(message = "Email is mandatory field")
  @EmailValidation
  private String email;

  @NotEmpty(message = "Token is mandatory field")
  private String token;
}

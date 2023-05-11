package br.com.smartinsoft.coursesplatform.domain.user.api.v1.request;

import br.com.smartinsoft.coursesplatform.config.validators.EmailValidation;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class TokenRequest {
  @NotEmpty(message = "Email is mandatory field")
  @EmailValidation
  private String email;


}

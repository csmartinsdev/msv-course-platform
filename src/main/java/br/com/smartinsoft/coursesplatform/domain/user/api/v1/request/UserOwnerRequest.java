package br.com.smartinsoft.coursesplatform.domain.user.api.v1.request;

import br.com.smartinsoft.coursesplatform.config.validators.CpfValidation;
import br.com.smartinsoft.coursesplatform.config.validators.EmailValidation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserOwnerRequest {
  @CpfValidation
  private String cpf;

  @NotEmpty(message = "Email is mandatory field")
  @EmailValidation
  private String email;

  @NotEmpty(message = "Name is mandatory field")
  private String name;

  @NotBlank(message = "password is mandatory field")
  private String password;

  public UserRequest converter() {
    return UserRequest.builder()
        .cpf(cpf)
        .name(name)
        .email(email)
        .password(password)
        .build();
  }
}

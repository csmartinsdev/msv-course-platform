package br.com.smartinsoft.coursesplatform.domain.auth.api.v1.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
  @NotBlank(message = "Login is mandatory field")
  private String login;

  @NotBlank(message = "Password is mandatory field")
  private String password;

  @Override
  public String toString() {
    return "AuthRequest{" +
        "login='" + login + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}

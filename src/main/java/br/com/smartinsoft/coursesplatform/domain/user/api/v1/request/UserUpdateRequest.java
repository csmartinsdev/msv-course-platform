package br.com.smartinsoft.coursesplatform.domain.user.api.v1.request;

import br.com.smartinsoft.coursesplatform.config.validators.CpfValidation;
import br.com.smartinsoft.coursesplatform.config.validators.EmailValidation;
import br.com.smartinsoft.coursesplatform.domain.role.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class UserUpdateRequest implements UserRequested {
  @CpfValidation
  private String cpf;

  @NotEmpty(message = "Email is mandatory field")
  @EmailValidation
  private String email;

  @NotEmpty(message = "Name is mandatory field")
  private String name;

  private String password;
  private Boolean active;
  private List<String> roles;

  public void removeRolesForbidden() {
    roles.remove(Role.OWNER);
    roles.remove(Role.CUSTOMER);
  }

  @Override
  public String toString() {
    return "UserUpdateRequest{" +
        "cpf='" + cpf + '\'' +
        ", email='" + email + '\'' +
        ", name='" + name + '\'' +
        ", password='" + password + '\'' +
        ", active=" + active +
        ", roles=" + roles +
        '}';
  }
}


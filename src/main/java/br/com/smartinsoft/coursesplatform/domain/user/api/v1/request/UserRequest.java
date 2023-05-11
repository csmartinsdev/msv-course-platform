package br.com.smartinsoft.coursesplatform.domain.user.api.v1.request;

import br.com.smartinsoft.coursesplatform.config.validators.CpfValidation;
import br.com.smartinsoft.coursesplatform.config.validators.EmailValidation;
import br.com.smartinsoft.coursesplatform.domain.role.entity.Role;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest implements UserRequested {
  @CpfValidation
  private String cpf;

  @NotEmpty(message = "Email is mandatory field")
  @EmailValidation
  private String email;

  @NotEmpty(message = "Name is mandatory field")
  private String name;

  @NotBlank(message = "password is mandatory field")
  private String password;
  private List<String> roles;


  public User converter() {
    User user = new User();
    user.setExternalId(UUID.randomUUID().toString());
    user.setCpf(cpf);
    user.setName(name);
    user.setEmail(email);
    user.setPassword(password);
    user.setCreatedDate(LocalDate.now());
    user.setActive(Boolean.FALSE);

    return user;
  }

  public void update(User user) {
    user.setCpf(cpf);
    user.setName(name);
    user.setEmail(email);
    user.setPassword(password);
  }

  @Override
  public String toString() {
    return "UserRequest{" +
        "cpf='" + cpf + '\'' +
        ", email='" + email + '\'' +
        ", name='" + name + '\'' +
        ", password='" + password + '\'' +
        ", roles=" + roles +
        '}';
  }

  public void removeRolesForbidden() {
    roles.remove(Role.OWNER);
    roles.remove(Role.CUSTOMER);
  }
}

package br.com.smartinsoft.coursesplatform.domain.user.api.v1.response;

import br.com.smartinsoft.coursesplatform.config.response.ModelResponse;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
public class UserResponse {
  private String externalId;
  private String cpf;
  private String name;
  private String email;
  private boolean active;
  private List<ModelResponse> roles;
  private String userOwnerExternalId;
  private String userOwnerName;


  public UserResponse converter(User user) {
    UserResponse userResponse = UserResponse.builder()
        .externalId(user.getExternalId())
        .cpf(user.getCpf())
        .name(user.getName())
        .email(user.getEmail())
        .active(user.isActive())
    .build();

   if (ObjectUtils.isNotEmpty(user.getOwner())) {
     userResponse.setUserOwnerExternalId(user.getOwner().getExternalId());
     userResponse.setUserOwnerName(user.getOwner().getName());
   }

   if (ObjectUtils.isNotEmpty(user.getRoles())) {
     userResponse.setRoles(user.getRoles().stream().map(role ->
         ModelResponse.builder()
             .value(role.getName())
             .build())
         .collect(Collectors.toList()));
   }

    return userResponse;
  }

}

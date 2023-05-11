package br.com.smartinsoft.coursesplatform.domain.user.listener.event;

import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import lombok.Data;

@Data
public class UserCreatedEmailEvent {
  private User user;

  public UserCreatedEmailEvent(User user) {
    this.user = user;
  }
}

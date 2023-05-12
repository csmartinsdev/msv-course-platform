package br.com.smartinsoft.coursesplatform.domain.user.listener.event;

import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import lombok.Data;
import org.springframework.context.ApplicationEvent;
@Data
public class TokenEmailEvent {
  private User user;
  private String token;
  public TokenEmailEvent(User user, String token) {
    this.user = user;
    this.token = token;
  }
}

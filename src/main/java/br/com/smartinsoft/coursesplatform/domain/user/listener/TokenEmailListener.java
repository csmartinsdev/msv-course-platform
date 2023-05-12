package br.com.smartinsoft.coursesplatform.domain.user.listener;

import br.com.smartinsoft.coursesplatform.config.component.MessageProperty;
import br.com.smartinsoft.coursesplatform.domain.user.listener.event.TokenEmailEvent;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

public class TokenEmailListener {
  @Autowired
  private MessageProperty messageProperty;

  @Async
  @EventListener
  public void sendEmail(TokenEmailEvent event) {
    HashMap<String, String> formEmail = new HashMap<>();
    formEmail.put("email", event.getUser().getUsername());
    formEmail.put("token", event.getToken());


  }

}

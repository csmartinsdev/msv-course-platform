package br.com.smartinsoft.coursesplatform.domain.user.listener;

import br.com.smartinsoft.coursesplatform.config.component.MessageProperty;
import br.com.smartinsoft.coursesplatform.domain.user.listener.event.UserCreatedEmailEvent;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserCreatedEmailListener {
  @Autowired
  private MessageProperty messageProperty;

  @Async
  @EventListener
  public void sendEmail(UserCreatedEmailEvent event) {
    HashMap<String, String> formEmail = new HashMap<>();
    formEmail.put("name", event.getUser().getName());
    formEmail.put("email", event.getUser().getEmail());
    formEmail.put("userExternalId", event.getUser().getExternalId());


  }

}

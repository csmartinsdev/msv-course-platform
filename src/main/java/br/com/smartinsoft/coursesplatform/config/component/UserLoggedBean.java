package br.com.smartinsoft.coursesplatform.config.component;

import br.com.smartinsoft.coursesplatform.config.service.JwtTokenService;
import java.io.Serializable;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserLoggedBean implements Serializable {
  private static final long serialVersionUID = -4231750944389343344L;

  @Autowired
  private transient HttpSession session;

  public String getOwnerExternalId() {
    return (String) session.getAttribute(JwtTokenService.OWNER_EXTERNAL_ID);
  }

  public String getUserExternalId() {
    return (String) session.getAttribute(JwtTokenService.USER_EXTERNAL_ID);
  }

}

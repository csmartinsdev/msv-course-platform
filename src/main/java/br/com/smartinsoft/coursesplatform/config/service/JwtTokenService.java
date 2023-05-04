package br.com.smartinsoft.coursesplatform.config.service;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtTokenService {
  String X_AUTHORIZATION = "X-Authorization";
  String OWNER_EXTERNAL_ID = "ownerExternalId";
  String USER_EXTERNAL_ID = "userExternalId";
  String ROLES_KEY = "roles";
  String PRIVILEGES_KEY = "privileges";

  String generateToken(UserDetails userDetails);
  String getSubject(String token);
  boolean validate(String token);
  List<GrantedAuthority> getAuthorities(HttpServletRequest request, String token);
  String genToken();
  String getOwnerExternalId(HttpServletRequest request);
  String getUserExternalId(HttpServletRequest request);

}

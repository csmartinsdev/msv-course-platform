package br.com.smartinsoft.coursesplatform.domain.auth.service;

import br.com.smartinsoft.coursesplatform.domain.auth.api.v1.request.AuthRequest;
import br.com.smartinsoft.coursesplatform.domain.auth.api.v1.response.AuthResponse;
import javax.servlet.http.HttpServletRequest;

public interface AuthService {

  AuthResponse auth(HttpServletRequest request, AuthRequest authRequest);

}

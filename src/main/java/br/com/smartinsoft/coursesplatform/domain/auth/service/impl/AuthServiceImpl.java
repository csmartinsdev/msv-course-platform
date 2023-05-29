package br.com.smartinsoft.coursesplatform.domain.auth.service.impl;

import br.com.smartinsoft.coursesplatform.config.service.JwtTokenService;
import br.com.smartinsoft.coursesplatform.domain.auth.api.v1.request.AuthRequest;
import br.com.smartinsoft.coursesplatform.domain.auth.api.v1.response.AuthResponse;
import br.com.smartinsoft.coursesplatform.domain.auth.service.AuthService;
import br.com.smartinsoft.coursesplatform.domain.refreshtoken.entity.RefreshToken;
import br.com.smartinsoft.coursesplatform.domain.refreshtoken.service.impl.RefreshTokenServiceImpl;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import br.com.smartinsoft.coursesplatform.domain.user.repository.UserRepository;
import br.com.smartinsoft.coursesplatform.exception.UserNotAuthorizedException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtTokenService jwtTokenService;
  @Autowired
  private RefreshTokenServiceImpl refreshTokenService;
  @Autowired
  private UserRepository userRepository;

  @Override
  public AuthResponse auth(HttpServletRequest request, AuthRequest authRequest) {
    Optional<User> userOptional = userRepository.findByEmail(authRequest.getLogin());
    if (userOptional.isEmpty()) {
      userOptional = userRepository.findByCpf(authRequest.getLogin());
    }

    if (userOptional.isPresent() && userOptional.get().isActive()) {
      User user = userOptional.get();

      if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
        RefreshToken refreshToken = refreshTokenService.processRefreshToken(user);
        return new AuthResponse(jwtTokenService.generateToken(user), refreshToken.getToken());
      }
    }

    throw new UserNotAuthorizedException();
  }
}

package br.com.smartinsoft.coursesplatform.domain.user.service.impl;

import br.com.smartinsoft.coursesplatform.config.component.MessageProperty;
import br.com.smartinsoft.coursesplatform.config.component.UserLoggedBean;
import br.com.smartinsoft.coursesplatform.config.service.impl.JwtTokenServiceImpl;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserOwnerRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.response.UserResponse;
import br.com.smartinsoft.coursesplatform.domain.user.repository.UserRepository;
import br.com.smartinsoft.coursesplatform.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
  @Autowired
  private MessageProperty messageProperty;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtTokenServiceImpl jwtTokenServiceImpl;

  @Autowired
  private UserLoggedBean userLoggedBean;

  @Autowired
  private UserRepository userRepository;


  @Override
  public UserResponse create(UserRequest request, boolean isOwner) {
    return null;
  }

  @Override
  public UserResponse createOwner(UserOwnerRequest request) {
    return null;
  }


}

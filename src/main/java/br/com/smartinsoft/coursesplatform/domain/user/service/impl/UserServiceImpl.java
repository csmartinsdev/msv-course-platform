package br.com.smartinsoft.coursesplatform.domain.user.service.impl;

import br.com.smartinsoft.coursesplatform.config.component.BeanUtilsNotNull;
import br.com.smartinsoft.coursesplatform.config.component.MessageProperty;
import br.com.smartinsoft.coursesplatform.config.component.UserLoggedBean;
import br.com.smartinsoft.coursesplatform.config.response.ApiServiceResponse;
import br.com.smartinsoft.coursesplatform.config.service.impl.JwtTokenServiceImpl;
import br.com.smartinsoft.coursesplatform.domain.role.entity.Role;
import br.com.smartinsoft.coursesplatform.domain.role.repository.RoleRepository;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.TokenRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.TokenValidationRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserOwnerRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserRequested;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserUpdateRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.response.UserResponse;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import br.com.smartinsoft.coursesplatform.domain.user.listener.event.TokenEmailEvent;
import br.com.smartinsoft.coursesplatform.domain.user.listener.event.UserCreatedEmailEvent;
import br.com.smartinsoft.coursesplatform.domain.user.repository.UserRepository;
import br.com.smartinsoft.coursesplatform.domain.user.service.UserService;
import br.com.smartinsoft.coursesplatform.exception.BusinessException;
import br.com.smartinsoft.coursesplatform.exception.ResourceNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
  private BeanUtilsNotNull beanUtilsNotNull;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private ApplicationEventPublisher publisher;

  @Transactional
  public UserResponse create(UserRequest request, boolean isOwner) {
    User user = validate(request);
    if (user == null) {
      user = request.converter();
    } else {
      request.update(user);
    }

    if (!CollectionUtils.isEmpty(request.getRoles())) {
      if (!isOwner) {
        request.removeRolesForbidden();
      }
      user.setRoles(new HashSet<>(roleRepository.findByNameIn(request.getRoles())));
    }

    if (isOwner) {
      String token = jwtTokenServiceImpl.genToken();
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      user.setTokenEmail(passwordEncoder.encode(token));
      user.setTokenEmailDate(LocalDateTime.now().plusMinutes(30));
    }
    user = userRepository.save(user);

    if (!isOwner) {
      user.setOwner(populateUserComposition(new User(userLoggedBean.getOwnerExternalId())));
      this.publisher.publishEvent(new UserCreatedEmailEvent(user));
    }

    return UserResponse.builder()
        .build()
        .converter(user);
  }

  @Transactional
  public UserResponse createOwner(UserOwnerRequest request) {
    UserRequest userRequest = request.converter();
    userRequest.setRoles(Arrays.asList(Role.OWNER));

    return create(userRequest, Boolean.TRUE);
  }
  @Transactional
  public UserResponse update(String externalId, UserUpdateRequest request) {
    validate(externalId, request);

    Optional<User> userOptional = userRepository.findByExternalId(externalId);
    if (userOptional.isPresent()) {
      User user = userOptional.get();

      validateUseCannotAccessUser(user);

      if (request.getRoles() != null && !request.getRoles().isEmpty()) {
        request.removeRolesForbidden();
        List<Role> roleList = roleRepository.findByNameIn(request.getRoles());
        Set<Role> roleSet = new HashSet<>(roleList);
        user.setRoles(roleSet);
      }
      request.setRoles(null);

      beanUtilsNotNull.copy(user, request);
      user.setUpdatedDate(LocalDate.now());

      if (!StringUtils.isBlank(request.getPassword())) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
      }

      if (!CollectionUtils.isEmpty(request.getRoles())) {
        user.setRoles(new HashSet<>(roleRepository.findByNameIn(request.getRoles())));
      }

      user = userRepository.save(user);
      return UserResponse.builder()
          .build()
          .converter(user);
    }

    throw new ResourceNotFoundException(messageProperty.getProperty("error.notFound",
        messageProperty.getProperty("user")));

  }

  @Override
  public ApiServiceResponse sendToken(TokenRequest request) {
    Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
    if (userOptional.isPresent()) {
      User user = userOptional.get();

      if (!StringUtils.isBlank(user.getOwnerExternalId())) {
        throw new BusinessException(messageProperty.getProperty("error.owner.canAccess"));
      }
      String token = jwtTokenServiceImpl.genToken();

      user.setTokenEmail(passwordEncoder.encode(token));
      user.setTokenEmailDate(LocalDateTime.now().plusMinutes(30));
      userRepository.save(user);

      this.publisher.publishEvent(new TokenEmailEvent(user, token));

      return ApiServiceResponse.builder()
          .id((long) HttpStatus.OK.value())
          .message(messageProperty.getProperty("success.token.sendEmail"))
          .build();
    }
    throw new ResourceNotFoundException(messageProperty.getProperty("error.notFound",
        messageProperty.getProperty("user")));
  }
  @Override
  public ApiServiceResponse validateToken(TokenValidationRequest request) {
    Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      if (passwordEncoder.matches(request.getToken(), user.getTokenEmail()) && user.getTokenEmailDate().isAfter(LocalDateTime.now())) {
        user.setActive(Boolean.TRUE);
        userRepository.save(user);

        return ApiServiceResponse.builder()
            .id((long) HttpStatus.OK.value())
            .message(messageProperty.getProperty("auth.validated", messageProperty.getProperty("user")))
            .build();
      }
    }
    throw new ResourceNotFoundException(messageProperty.getProperty("error.token.invalid"));
  }

  private void validateUseCannotAccessUser(User user) {
    if (!StringUtils.isBlank(userLoggedBean.getOwnerExternalId()) &&
        !userLoggedBean.getOwnerExternalId().equals(user.getOwnerExternalId())) {
      throw new AccessDeniedException(messageProperty.getProperty("error.cannotAccess",
          messageProperty.getProperty("user")));
    }
  }
  private User validate(UserRequested request) {
    return validate(StringUtils.EMPTY, request);
  }
  private User validate(String externalId, UserRequested request) {
    User user = null;

    Optional<User> userOptional = userRepository.findByCpf(request.getCpf());
    if (userOptional.isEmpty()) {
      userOptional = userRepository.findByEmail(request.getEmail());
    }
    if (userOptional.isPresent()) {
      if ((!externalId.isEmpty() && !externalId.equals(userOptional.get().getExternalId())) ||
          externalId.isEmpty() && userOptional.get().isActive() ) {
        throw new BusinessException(messageProperty.getProperty("error.alreadyExists",
            messageProperty.getProperty("user")));
      } else {
        user = userOptional.get();
      }
    }
    return user;
  }
  private User populateUserComposition(User user) {
    return userRepository.findByExternalId(user.getExternalId())
        .orElse(null);
  }

}

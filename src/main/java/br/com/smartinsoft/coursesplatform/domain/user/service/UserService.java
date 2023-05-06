package br.com.smartinsoft.coursesplatform.domain.user.service;

import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserOwnerRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.response.UserResponse;

public interface UserService {
  UserResponse create(UserRequest request, boolean isOwner);
  UserResponse createOwner(UserOwnerRequest request);
}

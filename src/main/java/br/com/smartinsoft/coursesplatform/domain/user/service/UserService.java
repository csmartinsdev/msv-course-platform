package br.com.smartinsoft.coursesplatform.domain.user.service;

import br.com.smartinsoft.coursesplatform.config.response.ApiServiceResponse;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.TokenRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.TokenValidationRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserOwnerRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.request.UserUpdateRequest;
import br.com.smartinsoft.coursesplatform.domain.user.api.v1.response.UserResponse;

public interface UserService {
  UserResponse create(UserRequest request, boolean isOwner);
  UserResponse createOwner(UserOwnerRequest request);
  UserResponse update(String externalId, UserUpdateRequest request);
  ApiServiceResponse sendToken(TokenRequest request);
  ApiServiceResponse validateToken(TokenValidationRequest request);
}

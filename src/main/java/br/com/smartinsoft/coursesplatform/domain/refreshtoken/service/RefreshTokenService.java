package br.com.smartinsoft.coursesplatform.domain.refreshtoken.service;

import br.com.smartinsoft.coursesplatform.domain.refreshtoken.entity.RefreshToken;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import java.util.Optional;

public interface RefreshTokenService {
  Optional<RefreshToken> findByToken(String token);
  RefreshToken processRefreshToken(User user);
  RefreshToken checkExpiration(RefreshToken token);
}

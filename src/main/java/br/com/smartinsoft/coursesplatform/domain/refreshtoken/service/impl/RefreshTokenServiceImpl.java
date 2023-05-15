package br.com.smartinsoft.coursesplatform.domain.refreshtoken.service.impl;

import br.com.smartinsoft.coursesplatform.config.component.MessageProperty;
import br.com.smartinsoft.coursesplatform.domain.refreshtoken.entity.RefreshToken;
import br.com.smartinsoft.coursesplatform.domain.refreshtoken.repository.RefreshTokenRepository;
import br.com.smartinsoft.coursesplatform.domain.refreshtoken.service.RefreshTokenService;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import br.com.smartinsoft.coursesplatform.domain.user.repository.UserRepository;
import br.com.smartinsoft.coursesplatform.exception.RefreshTokenException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
  @Value("${jwt.expiration.refreshToken}")
  private Long expirationRefreshToken;
  @Autowired
  private RefreshTokenRepository refreshTokenRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private MessageProperty messageProperty;


  @Override
  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }

  @Transactional
  public RefreshToken processRefreshToken(User user) {
    Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByUser(user);
    if (tokenOptional.isPresent()) {
      try {
        return checkExpiration(tokenOptional.get());
      } catch (RefreshTokenException e) {
        log.info("Must be generate new Refresh Token");
      }
    }
    deleteByUserId(user);
    return createRefreshToken(user);
  }

  @Override
  public RefreshToken checkExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new RefreshTokenException(messageProperty.getProperty("error.refreshToken.expired"));
    }
    return token;
  }

  private long deleteByUserId(User user) {
    return refreshTokenRepository.deleteByUser(user);
  }
  private RefreshToken createRefreshToken(User user) {
    RefreshToken refreshToken = new RefreshToken();

    refreshToken.setUser(user);
    refreshToken.setExpiryDate(Instant.now().plusMillis(expirationRefreshToken));
    refreshToken.setToken(UUID.randomUUID().toString());

    return refreshTokenRepository.save(refreshToken);
  }
}

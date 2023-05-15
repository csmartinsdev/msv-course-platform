package br.com.smartinsoft.coursesplatform.domain.refreshtoken.repository;

import br.com.smartinsoft.coursesplatform.domain.refreshtoken.entity.RefreshToken;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(String token);
  Optional<RefreshToken> findByUser(User user);
  long deleteByUser(User user);

}
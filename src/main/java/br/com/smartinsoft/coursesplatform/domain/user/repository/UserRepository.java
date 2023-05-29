package br.com.smartinsoft.coursesplatform.domain.user.repository;

import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
  @Query("SELECT u FROM User u " +
      "LEFT JOIN FETCH u.owner o " +
      "LEFT JOIN FETCH u.roles roles " +
      "LEFT JOIN FETCH roles.privileges " +
      "WHERE u.cpf = ?1")
  Optional<User> findByCpf(String cpf);

  @Query("SELECT u FROM User u " +
      "LEFT JOIN FETCH u.owner o " +
      "LEFT JOIN FETCH u.roles roles " +
      "LEFT JOIN FETCH roles.privileges " +
      "WHERE u.email = ?1")
  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u " +
      "LEFT JOIN FETCH u.owner o " +
      "LEFT JOIN FETCH u.roles roles " +
      "LEFT JOIN FETCH roles.privileges " +
      "WHERE u.externalId = ?1")
  Optional<User> findByExternalId(String externalId);

}

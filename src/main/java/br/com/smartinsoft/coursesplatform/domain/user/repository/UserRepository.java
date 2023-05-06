package br.com.smartinsoft.coursesplatform.domain.user.repository;

import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  @Query("SELECT u FROM User u" +
        "LEFT JOIN FETCH u.owner o " +
        "LEFT JOIN FETCH u.roles roles " +
        "LEFT JOIN FETCH roles.privileges " +
        "WHERE u.cpf = :cpf")
  Optional<User> findByCpf(String cpf);

  @Query("SELECT u FROM User u" +
      "LEFT JOIN FETCH u.owner o " +
      "LEFT JOIN FETCH u.roles roles " +
      "LEFT JOIN FETCH roles.privileges " +
      "WHERE u.email = :email")
  Optional<User> findByEmail(String email);

  @Query("SELECT u FROM User u" +
      "LEFT JOIN FETCH u.owner o " +
      "LEFT JOIN FETCH u.roles roles " +
      "LEFT JOIN FETCH roles.privileges " +
      "WHERE u.externalId = :externalId")
  Optional<User> findByExternalId(String externalId);


  @Query("SELECT u FROM User u" +
      "LEFT JOIN FETCH u.owner o " +
      "LEFT JOIN FETCH u.roles roles " +
      "LEFT JOIN FETCH roles.privileges " +
      "WHERE u.externalId = :externalId " +
      "AND o.externalId = :ownerExternalId")
  Optional<User> findByExternalId(String ownerExternalId, String externalId);


  @Query(value = "SELECT u FROM User u" +
      "LEFT JOIN FETCH u.owner o " +
      "LEFT JOIN FETCH u.roles roles " +
      "LEFT JOIN FETCH roles.privileges " +
      "WHERE o.externalId = :userOwnerExternalId",
      countQuery = "SELECT count(u) FROM User u " +
        "LEFT JOIN FETCH u.owner o " +
        "LEFT JOIN FETCH u.roles roles " +
        "LEFT JOIN FETCH roles.privileges " +
        "WHERE o.externalId = :userOwnerExternalId"
  )
  Page<User> findByOwnerId(String userOwnerExternalId, Pageable pageable);

}

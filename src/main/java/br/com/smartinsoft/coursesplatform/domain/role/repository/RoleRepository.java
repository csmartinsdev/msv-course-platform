package br.com.smartinsoft.coursesplatform.domain.role.repository;

import br.com.smartinsoft.coursesplatform.domain.role.entity.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface RoleRepository extends JpaRepository<Role, Long> {
  List<Role> findByNameIn(List<String> names);
}

package br.com.smartinsoft.coursesplatform.domain.role.entity;

import br.com.smartinsoft.coursesplatform.domain.privilege.entity.Privilege;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "roles", schema = "courses")
@Data
public class Role implements Serializable {
  private static final long serialVersionUID = 694513343542890409L;

  public static final String OWNER = "OWNER";
  public static final String CUSTOMER = "CUSTOMER";

  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String description;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "roles_privileges",
      schema = "courses",
      joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
  private Set<Privilege> privileges;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Role role = (Role) o;
    return id.equals(role.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

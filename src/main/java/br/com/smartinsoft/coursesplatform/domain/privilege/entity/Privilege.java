package br.com.smartinsoft.coursesplatform.domain.privilege.entity;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

  @Entity
  @Table(name = "privileges", schema = "digital")
  @Data
public class Privilege implements Serializable {
  private static final long serialVersionUID = 5067155471500532602L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Privilege privilege = (Privilege) o;
    return id.equals(privilege.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}

package br.com.smartinsoft.coursesplatform.domain.user.entity;

import br.com.smartinsoft.coursesplatform.domain.role.entity.Role;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Data
@Table(name = "users", schema = "courses", uniqueConstraints = {
    @UniqueConstraint(name = "cpf_unique", columnNames = { "cpf" }),
    @UniqueConstraint(name = "email_unique", columnNames = {"email"}),
    @UniqueConstraint(name = "external_id_unique", columnNames = {"external_id"})
})
public class User implements Serializable, UserDetails {
  private static final long serialVersionUID = -2186164869918695704L;

  public User() { }

  public User(String externalId) {
    this.externalId = externalId;
  }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "external_id")
  private String externalId;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private User owner;

  @Column(name = "cpf")
  private String cpf;

  @Column(name = "email")
  private String email;

  private String name;
  private String password;
  private boolean active;

  @ManyToMany
  @JoinTable(name = "users_roles",
      schema = "courses",
      joinColumns = @JoinColumn(name = "user_external_id", referencedColumnName = "external_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;


  public String getName() {
    if(!StringUtils.isBlank(name)){
      name = WordUtils.capitalize(name.toLowerCase());
    }
    return name;
  }

  public String getOwnerExternalId(){
    if(ObjectUtils.isEmpty(owner)){
      return externalId;
    }
    return owner.getExternalId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.emptyList();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return Boolean.TRUE;
  }

  @Override
  public boolean isAccountNonLocked() {
    return Boolean.TRUE;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return Boolean.TRUE;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }


}

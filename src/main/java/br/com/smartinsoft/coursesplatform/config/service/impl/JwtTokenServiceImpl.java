package br.com.smartinsoft.coursesplatform.config.service.impl;

import br.com.smartinsoft.coursesplatform.config.service.JwtTokenService;
import br.com.smartinsoft.coursesplatform.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class JwtTokenServiceImpl implements Serializable, JwtTokenService {
  private static final long serialVersionUID = 8364311274946096699L;
  @Autowired
  private transient PasswordEncoder passwordEncoder;

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration.accessToken}")
  private Long expirationAccessToken;


  @Override
  public String generateToken(UserDetails userDetails) {
    return doGenerateToken(userDetails);
  }

  @Override
  public String getSubject(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody().getSubject();
  }

  @Override
  public boolean validate(String token) {
    String username = getSubject(token);
    String xa = getId(token);
    return passwordEncoder.matches(username, xa) && !isExpired(token);
  }


  @Override
  public List<GrantedAuthority> getAuthorities(HttpServletRequest request, String token) {
    Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

    List<GrantedAuthority> roles = getGrantedAuthorities((List<String>) claims.get(ROLES_KEY));
    List<GrantedAuthority> privileges = getGrantedAuthorities((List<String>) claims.get(PRIVILEGES_KEY));

    List<GrantedAuthority> grants = new ArrayList<>();
    grants.addAll(roles);
    grants.addAll(privileges);

    return grants;
  }

  @Override
  public String genToken() {
    int maxQtyCharacters = 6;
    String[] characters = {
      "0", "1", "b", "2", "4", "5", "6", "7", "8",
      "9", "a", "b", "c", "d", "e", "f", "g", "h",
      "i", "j", "k", "l", "m", "n", "o", "p", "q",
      "r", "s", "t", "u", "v", "w", "x", "y", "z"
    };

    StringBuilder token = new StringBuilder();
    for (int i = 0; i < maxQtyCharacters; i++) {
      int index = (int) (Math.random() * characters.length);//NOSONAR
      token.append(characters[index]);
    }
    return token.toString().toUpperCase();
  }

  @Override
  public String getOwnerExternalId(HttpServletRequest request) {
    return null;
  }

  @Override
  public String getUserExternalId(HttpServletRequest request) {
    return null;
  }

  private List<GrantedAuthority> getGrantedAuthorities(List<String> auths){
    List<GrantedAuthority> authorities = new ArrayList<>();

    if(ObjectUtils.isNotEmpty(auths)){
      auths.forEach(a-> authorities.add(new SimpleGrantedAuthority(a)));
    }
    return authorities;
  }

  private Boolean isExpired(String token) {
    final Date expiration = getExpirationDateFromToken(token);
    return expiration.before(new Date());
  }

  private String getId(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJwt(token).getBody().getId();
  }

  private Date getExpirationDateFromToken(String token) {
    return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
  }

  private String doGenerateToken(UserDetails userDetails) {
    User user = (User) userDetails;
    Map<String, Object> claims = new HashMap<>();

    populateClaims(user, claims);

    return Jwts.builder()
        .setId(passwordEncoder.encode(user.getName()))
        .setSubject(user.getUsername())
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + expirationAccessToken))
        .signWith(SignatureAlgorithm.HS512, secret)
        .addClaims(claims)
        .compact();
  }

  private void populateClaims(User user, Map<String, Object> claims) {
    Set<String> roles = new HashSet<>();
    Set<String> privileges = new HashSet<>();

    claims.put(USER_EXTERNAL_ID, user.getExternalId());
    claims.put(OWNER_EXTERNAL_ID, user.getOwnerExternalId());

    user.getRoles().forEach(role->{
      role.getPrivileges().forEach(p-> privileges.add(p.getName()));
      roles.add("ROLE_"+role.getName());
    });

  }


}

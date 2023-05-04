package br.com.smartinsoft.coursesplatform.config.filter;

import br.com.smartinsoft.coursesplatform.config.service.JwtTokenService;
import br.com.smartinsoft.coursesplatform.config.service.impl.JwtTokenServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class AuthenticationUserRequestFilter extends OncePerRequestFilter {

  @Autowired
  private JwtTokenServiceImpl jwtTokenServiceImpl;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    final String requestTokenHeader = request.getHeader(jwtTokenServiceImpl.X_AUTHORIZATION);

    String username = null;

    if (requestTokenHeader != null) {
      try {
        username = jwtTokenServiceImpl.getSubject(requestTokenHeader);
      } catch (MalformedJwtException e) {
        logger.error("MalformedJwtException JWT Token "+ requestTokenHeader);
      } catch (IllegalArgumentException e) {
        logger.error("Unable to get JWT Token "+ requestTokenHeader);
      } catch (ExpiredJwtException e) {
        logger.error("JWT Token has expired " + requestTokenHeader);
      }
    }

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && Boolean.TRUE.equals(jwtTokenServiceImpl.validate(requestTokenHeader))) {
      UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
          new UsernamePasswordAuthenticationToken(username, null, jwtTokenServiceImpl.getAuthorities(request, requestTokenHeader));

      usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

      request.getSession().setAttribute(JwtTokenService.OWNER_EXTERNAL_ID, jwtTokenServiceImpl.getOwnerExternalId(request));
      request.getSession().setAttribute(JwtTokenService.USER_EXTERNAL_ID, jwtTokenServiceImpl.getUserExternalId(request));
    }

    filterChain.doFilter(request, response);
  }
}

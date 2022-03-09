package org.example.statisticsservice.security.services;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.example.modelproject.model.Role;
import org.example.modelproject.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.example.modelproject.model.ERole.ROLE_ADMIN;
import static org.example.modelproject.model.ERole.ROLE_USER;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Value("${jwtSecret}")
  private String jwtSecret;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
    Map<String,Object> userDetails = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    log.info("UserDetails of JWT: {}", userDetails.toString());
    String username = userDetails.get("username").toString();
    User user = new User(username, String.valueOf(userDetails.get("password")));
    user.setId(Long.valueOf(userDetails.get("id").toString()));
    List<String> authorities = (List) userDetails.get("authorities");
    Set<Role> roles = new HashSet<>();
    authorities.stream().forEach(authority -> {
      Role rol = new Role();
      if(authority.equalsIgnoreCase("ROLE_USER")){
        rol.setId(1);
        rol.setName(ROLE_USER);
        roles.add(rol);
      } else if (authority.equalsIgnoreCase("ROLE_ADMIN")) {
        rol.setId(2);
        rol.setName(ROLE_ADMIN);
        roles.add(rol);
      }
    });
    user.setRoles(roles);
    log.info("User of JWT: {}", user.toString());

    return UserDetailsImpl.build(user);
  }

}

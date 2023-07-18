package com.github.kondury.library.security;

import com.github.kondury.library.security.model.Privilege;
import com.github.kondury.library.security.model.Role;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PrivilegeRepository privilegeRepository;

    @Override
    @CircuitBreaker(name = "security-service")
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> {
                    var roles = user.getRoles();
                    var privileges = privilegeRepository.findByRoles(roles);
                    var authorities = getAuthorities(roles, privileges);
                    return org.springframework.security.core.userdetails.User
                            .withUsername(user.getUsername())
                            .password(user.getPassword())
                            .authorities(authorities)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found: username=" + username));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles, Collection<Privilege> privileges) {
        var roleNames = roles.stream().map(role -> "ROLE_" + role.getName());
        var privilegeNames = privileges.stream().map(Privilege::getName);
        return Stream.concat(roleNames, privilegeNames)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}

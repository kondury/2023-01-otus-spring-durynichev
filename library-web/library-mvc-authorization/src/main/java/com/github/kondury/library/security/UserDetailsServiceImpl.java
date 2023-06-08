package com.github.kondury.library.security;

import com.github.kondury.library.security.model.Privilege;
import com.github.kondury.library.security.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final PrivilegeRepository privilegeRepository;

    @Transactional(readOnly = true)
    @Override
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

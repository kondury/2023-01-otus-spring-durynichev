package com.github.kondury.library.security;

import com.github.kondury.library.security.model.Privilege;
import com.github.kondury.library.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    @Query("""
            select p
            from Role r
            join r.privileges p
            where r in :roles"""
    )
    Collection<Privilege> findByRoles(@Param("roles") Collection<Role> roles);
}

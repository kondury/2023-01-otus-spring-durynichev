package com.github.kondury.library.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Table(name = "privileges")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Privilege {

    @Id
    @Column(name = "privilege_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "privilege_name")
    private String name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

}

package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="roles")
@NoArgsConstructor
@Setter
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 권한별 id
    @Column(nullable = false)
    private String name; // 권한 명

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

}

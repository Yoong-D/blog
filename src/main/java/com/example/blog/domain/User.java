package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "users")
@NoArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 식별 id

    @Column(nullable = false, updatable = true, length = 50)
    private String username; // 사용자 id

    @Column(nullable = false, length = 100)
    private String password; // 비밀번호

    @Column(nullable = false, length = 50)
    private String name; // 이름(닉네임)

    @Column(nullable = false, length = 100)
    private String email; // 이메일

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now(); // 생성일

    @Column(name = "blog_name", length = 100, nullable = false)
    private String blog_name = username + "log"; // 블로그 명(default-id.log)

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

}

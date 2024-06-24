package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name="users")
@NoArgsConstructor
@Setter
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 사용자 식별 id
    @Column(nullable = false, updatable = true, length = 20)
    private String username; // 사용자 id
    @Column(nullable = false, length = 12)
    private String password; // 비밀번호
    @Column(nullable = false, length= 10)
    private String name; // 이름(닉네임)
    @Column(nullable = false)
    private String email; // 이메일
    private LocalDateTime registration_date; // 생성일
    private String blog_name; // 블로그 명

    public User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        registration_date = LocalDateTime.now();
    }

    @ManyToMany
    @JoinTable(name="user_roles",joinColumns = @JoinColumn(name="usernmaes"),
            inverseJoinColumns = @JoinColumn(name="id"))
    private Set<Role> roles = new HashSet<>();
}

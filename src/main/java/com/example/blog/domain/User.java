package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    @Column(name = "username", nullable = false, length = 50)
    private String username; // 사용자 id

    @Column(name = "password", nullable = false, length = 100)
    private String password; // 비밀번호

    @Column(name = "name", nullable = false, length = 100)
    private String name; // 이름(닉네임)

    @Column(name = "email", nullable = false, length = 100)
    private String email; // 이메일

    @Column(name = "registration_date", nullable = false, updatable = false)
    private LocalDateTime registrationDate = LocalDateTime.now(); // 생성일

    @Column(name = "blog_name", updatable = true, length = 100, nullable = false)
    private String blog_name = username + "log"; // 블로그 명(default-id.log)

    // 생성자
    public User(String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    // role과 다대다 관계
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    // 게시글과 일대다 관계
    // mappedBy(양방향 관계에서 사용) = "user" -> Post 엔티티에서 user 필드를 참조한다는 것을 의미
    // Post 엔티티의 author 필드가 외래 키를 가지고 있고, 관계의 소유자
    // cascade = CascadeType.ALL -> User 엔티티를 저장하면 해당 User와 연관된 모든 Post 엔티티도 함께 저장
    // orphanRemoval = true -> 더 이상 부모 엔티티와 연관되지 않으면(고아가 되면) 자동으로 삭제
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

}

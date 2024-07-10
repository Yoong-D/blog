package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="refresh_token")
@NoArgsConstructor
@Getter
@Setter
// 리프레쉬 토큰 저장소
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name="user_id")
    private String userId;

    @Column(nullable = false, name="value")
    private String value;
}

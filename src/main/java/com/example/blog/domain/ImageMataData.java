package com.example.blog.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "image_metadata")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageMataData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long id; // 이미지 id

    @Column(name="filename", nullable = false, length = 255)
    private String filename; // 이미지 명

    @Column(name="filepath", nullable = false, length = 255)
    private String filepath; // 이미지 경로

    @Column(name="uploadDate;")
    private LocalDateTime uploadDate = LocalDateTime.now(); // 이미지 업로드 날짜

    @Column(name="filesize")
    private Integer filesize; // 이미지 크기

    @Column(name="width")
    private Integer width; // 이미지 가로 크기

    @Column(name="height")
    private Integer height; // 이미지 세로 크기

    @Column(name="mimetype", length = 50)
    private String mimetype;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description; // 이미지 설명

    @Column(name="tags", length = 255)
    private String tags;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post; // 게시글 id

}

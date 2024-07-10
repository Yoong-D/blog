package com.example.blog;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

//
//    @Autowired
//    JwtTokenizer jwtTokenizer;
//    // 커멘드 라인 생성
//    @Bean
//    public CommandLineRunner run(){
//        return args -> {
//            String accessToken = jwtTokenizer.createAccessToken(
//                    1L, "test@test.com", "test", "testuser", Arrays.asList("ROLE_USER")
//            );
//
//            log.info("AccessToken {}" , accessToken);
//        };
//    }

}

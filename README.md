# 블로그 만들기  프로젝트
##### 프로젝트 진행 기간 : ~ 2024.07.19(10day) (단독)

##### 개발 도구
- **IntelliJ IDEA Professional**

##### 프레임워크 및 라이브러리
- **Spring Boot 3.3.1**
  - Spring Web
  - Spring Data JDBC
  - Spring Security 6
  - Spring JPA
- **Lombok**
- **Thymeleaf**
- **Validation**

##### 프로그래밍 언어 및 기술
- **Java** 21
- **HTML5**
- **CSS**

##### 데이터베이스
- **MySQL** 8.4.0

##### 인증 및 보안
- **JWT** (JSON Web Tokens)
  - Access Token
  - Refresh Token

##### API
- **REST API**

##### 도구 및 컨테이너화
- **Docker**

##### Editor
- **CKEditor5**



## 구현 모습 
##### 메인화면(로그인 전)
![image](https://github.com/user-attachments/assets/8fb62ca0-3b2f-4b03-a2ea-08eda29623f9)

##### 메인화면(로그인 후)
![image](https://github.com/user-attachments/assets/5b8bab86-565f-4aa7-93ed-66f6e9d5cff9)

##### 로그인
![image](https://github.com/user-attachments/assets/1e751df1-eb38-4ecf-9c79-7cbb4823f2a7)

##### 회원가입
![image](https://github.com/user-attachments/assets/b09fc457-60af-4395-bacb-4bd8688a0d55)

##### 글 상세보기
![image](https://github.com/user-attachments/assets/09bdd88a-3ad1-4ade-9421-d5c2970511dd)

##### 글 작성하기
![image](https://github.com/user-attachments/assets/b9fec515-7856-455d-ac29-667adaf90a9a)

##### 댓글 상세보기
![image](https://github.com/user-attachments/assets/de05c6d9-ec22-42d4-9ba2-ad621bd485f0)

##### 마이페이지(내 글만 보이기)
![image](https://github.com/user-attachments/assets/fa2e9299-605e-4987-a833-1fc205d68e11)


## 기능 구현
1. 회원가입  
  - [x] 회원 가입폼
  - [x] 같은 ID, Email Check API
  - [x] 회원가입 기능
  - [x] 회원 가입 후 로그인 폼으로 이동

2. 로그인/로그아웃
  - [x] 로그인 폼
  - [x] 로그인 기능
  - [x] 로그인 성공 후 "/"로 이동
  - [x] 로그인 실패 후 다시 폼으로 이동 (오류메시지출력)
  - Spring Security 를 이용해 로그인 구현
    - [x] Form Login
    - [x] JWT Login
  - [x] 로그아웃
   
3. 사이트 상단
  - [x] 사이트 로고가 좌측 상단에 보여짐
  - [x] 사이트 우측에는 로그인 링크 또는 사용자 정보가 보여짐
    - [x] 로그인을 안했을 경우엔 로그인 링크가 보여짐
    - [x] 로그인을 했을 경우엔 사용자 이름이 보여짐
    - [x] 사용자 이름을 클릭하면 설정, 해당 사용자 블로그 이동 링크, 임시 저장글 목록 보기, 로그 아웃이 있음
    
4. 메인 페이지(/)
  - [x] 블로그 글 목록 보기(최신 순, 조회수 높은 순, 즐겨찾기 순)
  - [x] 페이징 처리 또는 무한 스크롤 구현
  - [x] 제목, 내용, 사용자 이름 중에 하나로 검색기능

5. 블로그 글 쓰기
  - [x] 블로그 제목, 내용, 사진 등을 입력하여 글을 씀
        

6. 특정 사용자 블로그 글 보기(/@사용자아이디)
   - [x] 사용자 정보 보기
   - [x] 사용자가 쓴 글 목록 보기(과거순)
     - [x] 최신순
   - [x] 페이징 처리 또는 무한 스크롤 구현
   - [x] 제목, 내용 중에 하나로 검색 기능

7. 블로그 글 상세보기
    - [x] 메인 페이지에서 제목을 클릭할 때 블로그 글 상세 보여짐


8. 사용자 정보 보기
    - [x] 사이트 상단에서 로그인하였을 경우 보여지는 로그인 사용자 이름을 클릭하면 사용자 정보가 보여짐

9. 댓글 달기
    - [x] 블로그 게시글에 댓글을 달 수 있다.


10. 댓글 목록 보여주기
    - [x] 블로그 글 상세보기로 하단에 댓글 목록을 볼 수 있음


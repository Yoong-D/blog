const submitButton = document.getElementById('joinButton'); // 폼 제출 버튼
const idDuplicationBtn = document.getElementById('id_duplication'); // 아이디 중복 버튼
const emailDuplicationBtn = document.getElementById('email_duplication'); // 이메일 중복 버튼

let idVerificationBtn = false; // 아이디 중복 버튼을 눌렀는가?
let emailVerificationBtn = false; // 이메일 중복 버튼을 눌렀는가?
let idErrorMessage = "none"; // 아이디 관련 오류 메시지
let emailErrorMessage = "none"; // 이메일 관련 오류 메시지

// 폼 제출 이벤트 리스너 등록
submitButton.addEventListener('click', function (event) {
    event.preventDefault(); // 기본 제출 동작 방지

    // 입력된 값 가져오기
    const id = document.getElementById('username').value; // id
    const userName = document.getElementById('name').value; // 사용자 명
    const userEmail = document.getElementById('email').value; // 이메일
    const pw = document.getElementById('password').value; // 비밀번호
    const againPw = document.getElementById('again_password').value; // 비밀번호 확인

    // 사용자 회원가입 정보를 담은 객체 생성
    const joinUserDto = {
        username: id,
        password: pw,
        againPassword: againPw,
        name: userName,
        email: userEmail
    };

    // 중복 확인 관련 조건
    if (!idVerificationBtn && !emailVerificationBtn) {
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디와 이메일 중복 확인을 해주세요.');
    } else if (!idVerificationBtn) {
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디 중복 확인을 해주세요.');
    } else if (!emailVerificationBtn) {
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('이메일 중복 확인을 해주세요.');
    }

    // 중복 에러 메시지 처리
    if (idErrorMessage === "duplicate id" && emailErrorMessage === "duplicate email") {
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디와 이메일은 중복될 수 없습니다.');
    } else if (idErrorMessage === "duplicate id") {
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디는 중복될 수 없습니다.');
    } else if (emailErrorMessage === "duplicate email") {
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('이메일은 중복될 수 없습니다.');
    }

    // fetch API를 사용하여 POST 요청 보내기
    fetch('/api/userreg', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // 요청의 Content-Type을 JSON으로 설정
        },
        body: JSON.stringify(joinUserDto) // 요청 헤더 설정 ->   userLoginDto를 보내기
    }).then(response => {
        // Step 1: 서버 응답 처리
        if (response.ok) {
            // 성공적인 응답인 경우 JSON 형식의 데이터를 반환
            return response.json();
        } else {
            // 실패한 경우 에러 메시지를 포함하여 에러 throw
            return response.text().then(errorMessage => {
                throw new Error(errorMessage);
            });
        }
    })
        .then(data => {
            // Step 2: 서버에서 반환된 데이터 처리
            console.log('Response from server:', data);
            // 예시로 /welcome 페이지로 이동하는 코드
            window.location.href = '/loginform'; // 응답이 정상적일 경우 /welcome 페이지로 이동
        })
        .catch(error => {
            // Step 3: 에러 처리
            console.error('에러 메시지:', error.message);
            alert(error.message); // 에러 메시지를 사용자에게 알림
        });
});

// 아이디 중복 검증
idDuplicationBtn.addEventListener('click', function (event) {
    const username = document.getElementById('username').value; // 입력된 아이디 값
    idVerificationBtn = true; // 아이디 중복 버튼을 눌렀음을 표시
    event.preventDefault(); // 기본 제출 동작 방지
    fetch(`/api/users/check-username?username=${username}`)
        .then(response => {
            if (!response.ok) {
                // 응답이 ok가 아니면 텍스트로 에러 메시지를 읽음
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
            return response.json(); // 정상적인 경우 JSON 데이터를 파싱하여 반환
        })
        .then(data => {
            // 중복되는 아이디가 없음을 알림
            if (!data.exists) {
                idErrorMessage = "none";
                alert('해당 아이디는 사용 가능합니다.');
            }
        })
        .catch(error => {
            // 중복된 아이디가 있음을 알림
            idErrorMessage = "duplicate id";
            alert(error.message);
        });
});

// 이메일 중복 검증
emailDuplicationBtn.addEventListener('click', function (event) {
    const email = document.getElementById('email').value; // 입력된 이메일 값
    emailVerificationBtn = true; // 이메일 중복 버튼을 눌렀음을 표시
    event.preventDefault(); // 기본 제출 동작 방지
    fetch(`/api/users/check-email?email=${email}`)
        .then(response => {
            if (!response.ok) {
                // 응답이 ok가 아니면 텍스트로 에러 메시지를 읽음
                return response.text().then(errorMessage => {
                    throw new Error(errorMessage);
                });
            }
            return response.json(); // 정상적인 경우 JSON 데이터를 파싱하여 반환
        })
        .then(data => {
            // 중복되는 이메일이 없음을 알림
            if (!data.exists) {
                emailErrorMessage = "none";
                alert('해당 이메일은 사용 가능합니다.');
            }
        })
        .catch(error => {
            // 중복된 이메일이 있음을 알림
            emailErrorMessage = "duplicate email";
            alert(error.message);
        });
});


const submit = document.getElementById("loginButton"); // 제출 버튼

submit.addEventListener('click', function(event) {
    event.preventDefault(); // 기본 폼 제출 동작을 막음
    const username = document.getElementById('username').value;
    const id = document.getElementById("username").value; // 폼에 입력한 id값 가져오기
    const pw = document.getElementById("password").value; // 폼에 입력한 pw값 가져오기

    // 사용자 정보를 담은 객체 생성
    const userLoginDto = {
        username: id,
        password: pw
    };



    // fetch API를 사용하여 POST 요청 보내기
    fetch('/api/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' // 요청의 Content-Type을 JSON으로 설정
        },
        body: JSON.stringify(userLoginDto) // 요청 헤더 설정 ->   userLoginDto를 보내기
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
            window.location.href = "/"// 응답이 정상적일 경우 메인 홈 페이지로 이동
        })
        .catch(error => {
            // Step 3: 에러 처리
            console.error('에러 메시지:', error.message);
            alert(error.message); // 에러 메시지를 사용자에게 알림
        });
});

const submitButton = document.getElementById('submit');
const id_duplication = document.getElementById('id_duplication'); // 아이디 중복 버튼
const email_duplication = document.getElementById('email_duplication');// 이메일 중복 버튼
const error_message = document.getElementById('error_message'); // 에러 메시지 input

let id_verification_btn = false; // 아아디 중복 버튼을 눌렀는가?
let email_verification_btn = false; // 이메일 중복 버튼을 눌렀는가?
let id_error_message = "none"; // 아이디 관련 메시지
let email_error_message="none";// 이메일 관련 메시지

// 아이디 중복 검증
id_duplication.addEventListener('click', function (event) {
    let username = document.getElementById('username').value; // id값 가져오기
    id_verification_btn = true;
    event.preventDefault(); // 기본 폼 제출 동작을 막음
    fetch(`/api/users/check-username?username=${username}`)
        .then(response => {
            if (!response.ok) {
                // 응답이 ok가 아니면 텍스트로 에러 메시지를 읽는다 -> rest api에서 에러 처리부분
                // 에러를 Error 객체로 감싸서 throw하면 catch 블록에서 error.message를 통해 해당 에러 메시지를 받을 수 있다.
                return response.text().then(error_message => {
                    throw new Error(error_message)
                });
            }
            return response.json(); // 서버에서 받은 응답이 ok라면  JSON 데이터를 파싱하여 JavaScript 객체로 반환
        })
        .then(data => { // 200 ok
            // JSON 객체에 exists 값이 false라면 중복이 없는것.
            // 중복된 것은 api에서 에러로 처리하여 catch 블록에서 처리할 것
            if (!data.exists) {
                error_message.value = "none";
                id_error_message = "none";
                alert('해당 아이디는 사용가능합니다.');

            }
        })
        .catch(error => { // bad request error
            // 아이디 중복 알림창 alert을 무시하고, 제출 버튼을 클릭시 error.html에 해당 오류가 출력될 예정.
                error_message.value  = error.message; // restapi에서 받은 에러 메시지 넣기
                id_error_message = "duplicate id";
            alert(error.message);
        });
});

// 이메일 중복 검증
email_duplication.addEventListener('click', function (event) {
    email_verification_btn = true;
    let email = document.getElementById('email').value; // id값 가져오기
    event.preventDefault(); // 기본 폼 제출 동작을 막음
    fetch(`/api/users/check-email?email=${email}`)
        .then(response => {
            if (!response.ok) {
                // 응답이 ok가 아니면 텍스트로 에러 메시지를 읽는다 -> rest api에서 에러 처리부분
                // 에러를 Error 객체로 감싸서 throw하면 catch 블록에서 error.message를 통해 해당 에러 메시지를 받을 수 있다.
                return response.text().then(error_message => {
                    throw new Error(error_message)
                });
            }
            return response.json(); // 서버에서 받은 응답이 ok라면  JSON 데이터를 파싱하여 JavaScript 객체로 반환
        })
        .then(data => { // 200 okconsole.log(data);
            // JSON 객체에 exists 값이 false라면 중복이 없는것.
            // 중복된 것은 api에서 에러로 처리하여 catch 블록에서 처리할 것
            if (!data.exists) {
                error_message.value = "none";
                email_error_message  = "none";
                alert('해당 이메일은 사용가능합니다.');
            }
        })
        .catch(error => { // bad request error
            error_message.value  = error.message; // restapi에서 받은 에러 메시지 넣기
            email_error_message = "duplicate email";
            alert(error.message);
        });
});
// 아이디나, 이메일을 값이 없다면, alert 띄우기
// 중복 버튼을 누르지 않는다면, 폼 제출 막기 + alert 띄우기
// 중복되는 값이 있다면, 폼 제출 막기(이메일, 아이디) + alert 띄우기
// 비밀번호와, 비밀번호 확인 값이 다를 경우 에러 메시지 설정
submitButton.addEventListener('click', function (event) {
    const password = document.getElementById('password').value; // 비밀번호
    const again_password = document.getElementById('again_password').value; // 비밀번호 확인

    if(id_verification_btn === false && email_verification_btn === false){
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디와 이메일 중복 확인을 해주세요.');
    }
    else if(id_verification_btn === false){
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디 중복 확인을 해주세요.');
    }
    else if(email_verification_btn === false){
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('이메일 중복 확인을 해주세요.');
    }

    if(id_error_message === "duplicate id" && email_error_message === "duplicate email"){
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디와 이메일은 중복될 수 없습니다. ');
    }
    else if(id_error_message === "duplicate id"){
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('아이디는 중복될 수 없습니다.');
    }
    else if(email_error_message === "duplicate e-mail"){
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('이메일은 중복될 수 없습니다.');
    }
    // 비밀번호와 확인 비밀번호가 다를 경우, error 페이지로 이동
    if (password !== again_password) {
        // error.html 에서 표시될 에러 페이지
        error_message.value = "비밀번호와 비밀번호 확인 값이 다릅니다.<br> 다시 회원가입을 진행해주세요.";
    }
});

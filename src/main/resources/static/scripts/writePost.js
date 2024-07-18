const post_submit = document.getElementById("post_submit");

// 제출 버튼 클릭시 일어날 이벤트 처리
post_submit.addEventListener('click', function (event){
    event.preventDefault(); // 기본 폼 제출 동작을 막음
    const post_title = document.getElementById("post_title").value;
    const post_tag = document.getElementById("post_tag").value;
    const post_series = document.getElementById("post_series").value;
    const post_content = window.editor.getData();

    // 작성된 포스트 정보를 담은 객체 생성
    const postDto = {
        title: post_title, // 제목
        content: post_content, // 내용
        tag: post_tag , // 태크
        series: post_series // 시리즈
    };

    fetch('/api/postform', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(postDto) // postDto를 JSON 문자열로 변환하여 전송
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // 성공적인 응답이면 JSON 데이터 반환
            } else {
                return response.text().then(errorMessage => {
                    alert(errorMessage); // 에러 메시지를 사용자에게 알림
                });
            }
        })
        .then(data => {
            // Step 2: 서버에서 반환된 데이터 처리
            console.log('Response from server:', data);
            window.location.href = '/'; // 응답이 정상적일 경우 / 페이지로 이동
        })
        .catch(error => {
            // Step 3: 에러 처리
            console.error('에러 메시지:', error.message);
            alert(error.message); // 에러 메시지를 사용자에게 알림
        });

});

document.getElementById("logoImg").onclick = function () {
    window.location.href = "/"; // /home으로 페이지 이동
};

// 삭제 버튼 클릭스 삭제 여부를 물어보고 삭제하기
function removeCheck() {
    let username = document.getElementById("author").innerText;
    username = username.replace('by. ', '').trim();
    let title = document.getElementById("title").innerText;
    title = title.trim();
    if (confirm("정말 삭제하시겠습니까??")) {
        alert("삭제되었습니다.");
        window.location.href = "/delete/" + encodeURIComponent(username) + "/" + encodeURIComponent(title); // URL 인코딩
    } else {
        // 취소 버튼 클릭 시 아무 동작 없음
    }
}

// 댓글 제출
document.getElementById("commentBtn").addEventListener('click', function (event){
    event.preventDefault(); // 기본 폼 제출 동작을 막음
    const title = document.getElementById("title").innerText; //게시물명
    let post_writer= document.getElementById("author").innerText;
    post_writer = post_writer.replace("by. ","").trim();
    const userid = document.getElementById("commentId").innerText; // 작성자 아아디
    const user = document.getElementById("writer").innerText; // 작성자 명
    const commentForm = document.getElementById("commentForm").value; // 댓글 작성

    if(commentForm.length<1){
        event.preventDefault(); // 기본 폼 제출 동작을 막음
        alert('댓글 내용을 입력해주세요.');
    }
    const commentDto = {
        // 게시글 제목
        postTitle: title,
        // 게시글 작성자
        username: post_writer,
        // 댓글 작성자 id
        writer: userid,
        // 댓글 작성자 명
        name: user,
        // 댓글 내용
        comments: commentForm
    };
    fetch('/api/comment', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(commentDto) // postDto를 JSON 문자열로 변환하여 전송
    })
        .then(response => {
            if (response.ok) {
                return response.json(); // 성공적인 응답이면 JSON 데이터 반환
            } else {
                // HTTP 상태 코드에 따라 처리
                if (response.status === 400) {
                    // Bad Request (400) 에러 처리
                    return response.json().then(data => {
                        throw new Error(data.message); // 서버에서 반환된 에러 메시지를 throw
                    });
                } else {
                    throw new Error('서버 오류 발생'); // 기타 HTTP 오류 처리
                }
            }
        })
        .then(data => {
            // Step 2: 서버에서 반환된 데이터 처리
            console.log('Response from server:', data);
            window.location.href = `/@${post_writer}/${title}`; // 응답이 정상적일 경우 해당 게시물로 이동
        })
        .catch(error => {
            // Step 3: 에러 처리
            console.error('에러 메시지:', error.message);
            alert(error.message); // 에러 메시지를 사용자에게 알림
        });
})
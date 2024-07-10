function refreshAccessToken() {
    console.log('Refreshing Access Token');

    // /refreshToken 엔드포인트에 POST 요청을 보냄
    fetch('/refreshToken', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // 요청의 Content-Type을 JSON으로 설정
        },
        body: JSON.stringify({}) // Refresh Token은 보내지 않음
    })
        .then(response => {
            // 서버 응답이 정상적인 경우
            if (response.ok) {
                return response.json(); // JSON 형식의 응답 데이터를 반환
            } else {
                throw new Error('Failed to refresh Access Token'); // 실패 시 에러 throw
            }
        })
        .then(data => {
            // 서버에서 반환된 데이터에 Access Token이 포함된 경우
            if (data.accessToken) {
                console.log('Access Token refreshed successfully');
                localStorage.setItem('lastRefreshTime', new Date().getTime()); // 마지막 갱신 시간을 localStorage에 저장
            } else {
                console.log('Failed to refresh Access Token'); // Access Token 갱신 실패 로그
            }
        })
        .catch(error => {
            console.error('Error:', error); // 에러 발생 시 에러 로그 출력
        });
}

function initTokenRefresh() {
    console.log('Initializing Token Refresh');

    // localStorage에서 마지막 갱신 시간을 가져옴
    const lastRefreshTime = localStorage.getItem('lastRefreshTime');
    const currentTime = new Date().getTime(); // 현재 시간을 milliseconds로 가져옴
    const timeElapsed = currentTime - lastRefreshTime; // 마지막 갱신 후 경과 시간 계산

    // 마지막 갱신 후 25분이 지났다면 즉시 갱신
    if (timeElapsed >= 25 * 60 * 1000) {
        refreshAccessToken();
    }

    // 매 1분마다 마지막 갱신 후 경과 시간을 체크하여 Access Token 갱신
    setInterval(() => {
        const lastRefreshTime = localStorage.getItem('lastRefreshTime');
        const currentTime = new Date().getTime();
        const timeElapsed = currentTime - lastRefreshTime;

        // 25분이 지난 경우에만 갱신
        if (timeElapsed >= 25 * 60 * 1000) {
            refreshAccessToken();
        }
    }, 1 * 60 * 1000); // 매 1분마다 체크
}

console.log('Script loaded');
// 페이지 로드 시 초기화 함수 호출
window.onload = initTokenRefresh;

// 현재 페이지의 URL을 가져옵니다.
let url = window.location.href;

// URL에서 마지막 경로 부분을 추출합니다.
let pathVariables = url.split('/');

// 배열의 마지막 요소가 PathVariable 값입니다.
let roomId = pathVariables[pathVariables.length - 1];

var ws = new WebSocket("wss://" + location.host + "/mainChat/" + roomId);

ws.onmessage = function (event) {
    var message = JSON.parse(event.data);

    var type = message.chatType;
    var username = message.userName;
    var content = message.content;
    var time = message.sendTime;

    if (type === "CHAT_TYPE") {
        // div 요소 생성
        const messageDiv = document.createElement('div');
        messageDiv.classList.add('message');

        const userName = document.createElement("p")
        userName.classList.add('fw-bold')
        userName.textContent = username;
        messageDiv.appendChild(userName);

        // message-time 클래스를 가진 div 요소 생성
        const messageTime = document.createElement('span');
        messageTime.classList.add('message-time');
        messageTime.textContent = time;
        messageTime.style.color = "grey"; // 회색으로 텍스트 색상 설정
        messageTime.style.marginLeft = "3px"; // 이름 오른쪽에 배치하기 위해 marginLeft를 auto로 설정
        userName.appendChild(messageTime);

        // message-bubble 클래스를 가진 div 요소 생성
        const messageBubble = document.createElement('div');
        messageBubble.classList.add('message-bubble');
        messageBubble.textContent = content;
        messageDiv.appendChild(messageBubble);

        document.getElementById("chat-window").appendChild(messageDiv);
    } else if (type === "USER_LIST") {
        const item = document.getElementById("user-list").querySelectorAll("#user-list-item");
        if (item != null) {
            item.forEach(function (element) {
                element.remove();
            });
        }
        const list = content.replaceAll("[", "").replaceAll("]", "")
            .replaceAll("\"", "").replaceAll(" ", "").split(",");
        list.forEach(function (element) {
            var p = document.createElement("p");
            p.style.margin = "0";
            p.id = "user-list-item"
            p.appendChild(document.createTextNode(element));
            document.getElementById("user-list-p").appendChild(p);
            document.getElementById("user-list-count").textContent = "유저 리스트(" + list.length + ")";
        });
    }
};

function sendMessage() {
    var input = document.getElementById("message");
    var message = input.value;
    ws.send(message)
    input.value = "";
}

document.getElementById("message").addEventListener("keydown", function (event) {
    if (event.keyCode === 13) { // Enter 키의 keyCode는 13입니다.
        sendMessage();
    }
});

// 스크롤을 항상 가장 아래로 이동하는 함수를 정의합니다.
function scrollToBottom() {
    var chatWindow = document.getElementById("chat-window");
    chatWindow.scrollTop = chatWindow.scrollHeight;
}

// 페이지 로드가 완료되면 scrollToBottom 함수를 실행합니다.
window.onload = function () {
    scrollToBottom();
};

// 새로운 채팅 메시지가 추가될 때마다 scrollToBottom 함수를 실행합니다.
// 예를 들어 새로운 채팅 메시지가 도착할 때마다 스크롤이 자동으로 아래로 이동합니다.
document.getElementById("chat-window").addEventListener("DOMNodeInserted", function () {
    scrollToBottom();
});

//유저 리스트 모달창
document.getElementById('user-list-toggle').addEventListener('click', function () {
    var userList = document.getElementById('user-list');
    if (userList.classList.contains('hidden')) {
        userList.classList.remove('hidden');
        userList.style.right = '0px'; // 모달 창이 오른쪽에서 왼쪽으로 나타남
    } else {
        userList.style.right = '-300px'; // 모달 창이 왼쪽으로 사라짐
        setTimeout(function () {
            userList.classList.add('hidden');
        }, 300); // 애니메이션이 끝난 후 hidden 클래스 추가
    }
});

document.getElementById('close-user-list').addEventListener('click', function () {
    var userList = document.getElementById('user-list');
    userList.style.right = '-300px'; // 모달 창이 왼쪽으로 사라짐
    setTimeout(function () {
        userList.classList.add('hidden');
    }, 300); // 애니메이션이 끝난 후 hidden 클래스 추가
});

let mediaRecorder;
let currentStream;

let player;

document.addEventListener('DOMContentLoaded', function() {
    if (videojs.browser.IS_IOS || videojs.browser.IS_ANDROID) {
        videoElement.src = '/livestream/videos/' + roomId + '/' + roomId + '.m3u8';

        videoElement.addEventListener('error', function() {
            console.log("Error occurred, retrying...");
            videoElement.load();
        });

        videoElement.addEventListener('loadedmetadata', function() {
            videoElement.play();
        });
    } else {
        const options = {
            html5: {
                hls: {
                    overrideNative: true,
                    overrideNativeMediaElements: true,
                    smoothQualityChange: true,
                    autoStartLoad: true,
                    debug: true, // 디버그 모드 활성화
                }
            },
            startLevel: 4,
            controls: true,
            autoplay: true,
            preload: 'auto',
            liveui: true,
            withCredentials: false,
            liveSyncDurationCount: 3, // 라이브 스트리밍의 경우 사용자가 현재 시간보다 이전의 세그먼트를 로드할 수 있는 시간
            maxBufferLength: 48, // 버퍼 최대 길이를 설정 (초 단위, 세그먼트 길이 * 세그먼트 수 = 12초 * 4 = 48초)
            maxMaxBufferLength: 60 // 최대 버퍼 길이를 설정 (초 단위)
        };

        player = videojs('videoPlayer', options);

        player.on('error', function() {
            console.log("Error occurred, retrying...");
            setTimeout(loadM3U8, 3000);
        });

        player.on('loadedmetadata', function() {
            player.play();
        });

        loadM3U8();
    }
});

function loadM3U8() {
    player.src({
        src: '/livestream/videos/' + roomId + '/' + roomId + '.m3u8',
        type: 'application/x-mpegURL'
    });
}

var ws2 = new WebSocket("wss://" + location.host + "/livestream/" + roomId);

ws2.onopen = function () {
    console.log('화면공유 서버 연결 성공.');
};

ws2.onmessage = function(event) {
    console.log(event.data);
};

async function startSharing() {
    document.getElementById('start-share').disabled = true;
    document.getElementById('stop-share').disabled = false;

    try {
        const stream = await navigator.mediaDevices.getDisplayMedia({
            video: {
                cursor: "always"
            },
            audio: true
        });

        currentStream = stream;

        startRecording(stream);

    } catch (error) {
        console.error("Error starting screen sharing:", error);
    }
}

function stopSharing() {
    mediaRecorder.stop();
    document.getElementById('start-share').disabled = false;
    document.getElementById('stop-share').disabled = true;

    const tracks = currentStream.getTracks();

    tracks.forEach(track => {
        track.stop();
    });
}

function startRecording(stream) {
    let options = {
        mimeType: 'video/webm; codecs="av01.0.05M.08, opus"'  // 기본 비디오 코덱 설정
    };
    mediaRecorder = new MediaRecorder(stream, options);

    mediaRecorder.ondataavailable = event => {
        if (event.data.size > 0) {
            if (ws2.readyState === WebSocket.OPEN) {
                ws2.send(event.data);
            }

        }
    };
    setInterval(() => {
        mediaRecorder.stop();
        mediaRecorder.start();
    }, 3000); // 2000ms = 2초
}

function toggleFullScreen() {
    if (!document.fullscreenElement) {
        if (videoElement.requestFullscreen) {
            videoElement.requestFullscreen();
        } else if (videoElement.mozRequestFullScreen) { /* Firefox */
            videoElement.mozRequestFullScreen();
        } else if (videoElement.webkitRequestFullscreen) { /* Chrome, Safari & Opera */
            videoElement.webkitRequestFullscreen();
        } else if (videoElement.msRequestFullscreen) { /* IE/Edge */
            videoElement.msRequestFullscreen();
        }
    } else {
        if (document.exitFullscreen) {
            document.exitFullscreen();
        } else if (document.mozCancelFullScreen) { /* Firefox */
            document.mozCancelFullScreen();
        } else if (document.webkitExitFullscreen) { /* Chrome, Safari & Opera */
            document.webkitExitFullscreen();
        } else if (document.msExitFullscreen) { /* IE/Edge */
            document.msExitFullscreen();
        }
    }
}



var ws = new WebSocket("ws://" + location.host + "/mainChat");
ws.onmessage = function(event) {
    var message = event.data;
    var p = document.createElement("p");
    p.appendChild(document.createTextNode(message));
    document.getElementById("chat").appendChild(p);
};

function sendMessage() {
    var input = document.getElementById("message");
    var message = input.value;
    ws.send(message);
    input.value = "";
}

document.getElementById("message").addEventListener("keydown", function(event) {
    if (event.keyCode === 13) { // Enter 키의 keyCode는 13입니다.
        sendMessage();
    }
});
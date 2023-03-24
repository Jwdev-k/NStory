var ws = new WebSocket("ws://" + location.host + "/realTime");
ws.onmessage = function(event) {
    var time = event.data;
    document.getElementById("serverTime").innerHTML = time;
};
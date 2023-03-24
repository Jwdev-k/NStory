var ws = new WebSocket("wss://nstory.xyz" + "/realTime");
ws.onmessage = function(event) {
    var time = event.data;
    document.getElementById("serverTime").innerHTML = time;
};
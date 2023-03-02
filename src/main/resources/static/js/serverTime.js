//서버시간 불러오기
var xmlHttp;

function srvTime(){

    if (window.XMLHttpRequest) {//분기하지 않으면 IE에서만 작동된다.

        xmlHttp = new XMLHttpRequest(); // IE 7.0 이상, 크롬, 파이어폭스 등

        //헤더 정보만 받기 위해 HEAD방식으로 요청.
        xmlHttp.open('HEAD', window.location.href.toString(), false); // window.location.href.toString() or Target URL

        xmlHttp.setRequestHeader("Content-Type", "text/html");
        xmlHttp.send('')

        return xmlHttp.getResponseHeader("Date");   //받은 헤더정보에서 Date 속성만 적출

    }else if (window.ActiveXObject) {
        xmlHttp = new ActiveXObject('Msxml2.XMLHTTP');
        xmlHttp.open('HEAD',window.location.href.toString(),false);
        xmlHttp.setRequestHeader("Content-Type", "text/html");
        xmlHttp.send('');
        return xmlHttp.getResponseHeader("Date");
    }
}

function dpTime() {
    //var now = new Date();
    var now = new Date(srvTime());
    hours = now.getHours();
    minutes = now.getMinutes();
    seconds = now.getSeconds();

    if (hours > 12) {
        hours -= 12;
        ampm = "오후 ";
    } else {
        ampm = "오전 ";
    }
    if (hours < 10) {
        hours = "0" + hours;
    }
    if (minutes < 10) {
        minutes = "0" + minutes;
    }
    if (seconds < 10) {
        seconds = "0" + seconds;
    }
    document.getElementById("dpTime").innerHTML = "현재 시간 ( " + ampm + hours + ":" + minutes + ":" + seconds + " )";
}

setInterval("dpTime()", 100);

/*
var st = srvTime();         // 한국 시간 -9
var today = new Date(st);   // 한국 시간 +9

alert(st);
alert(today);*/

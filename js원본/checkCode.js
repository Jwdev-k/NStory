$(document).ready(function() {
    $("#submit-button").click(function() {
        $("#form").hide();
        $("#loading").show();

        var formData = $("#form-data").serialize();

        $.ajax({
            type: "POST",
            url: "/check_code",
            data: formData,
            dataType: "json",
            success: function(response) {
                if (response.result == "success") {
                    alert(response.msg);
                    window.location.href = response.redirectUrl;
                } else if (response.result == "expired") {
                    alert(response.error);
                    window.location.href = response.redirectUrl;
                } else if (response.result == "signup") {
                    window.location.href = response.redirectUrl;
                } else {
                    $("#form").show();
                    $("#loading").hide();
                    document.getElementById("error").innerHTML = response.error;
                }
            },
            error: function(xhr, status, error) {
                console.log("Error: " + error);
            }
        });
    });
});

var countdown = 300; // 초 단위 카운트다운 시간
var timerElement = document.getElementById("timer"); // 타이머 표시할 요소
var intervalId = setInterval(function() {
    var minutes = Math.floor(countdown / 60);
    var seconds = countdown % 60;
    timerElement.innerHTML = ("0" + minutes).slice(-2) + ":" + ("0" + seconds).slice(-2);
    countdown--;
    if (countdown < 0) {
        clearInterval(intervalId);
        timerElement.innerHTML = "시간 초과";
        // 타이머가 종료될 때 보안코드 입력 폼을 비활성화
        document.getElementsByName("securityCode")[0].setAttribute("disabled", true);
        document.getElementsByTagName("input")[1].setAttribute("disabled", true);
        document.getElementById("timer").style.color = "red";
    }
}, 1000);
$(document).ready(function() {
    var userInputId = getCookie("userInputId");
    var setCookieYN = getCookie("setCookieYN");

    if(setCookieYN === 'Y') {
        $("#saveEmail").prop("checked", true);
    } else {
        $("#saveEmail").prop("checked", false);
    }

    $("#email").val(userInputId);

    //로그인 버튼 클릭
    $('#login_btn').click(function() {
        if($("#saveEmail").is(":checked")){
            var userInputId = $("#email").val();
            setCookie("userInputId", userInputId, 60);
            setCookie("setCookieYN", "Y", 60);
        } else {
            deleteCookie("userInputId");
            deleteCookie("setCookieYN");
        }

        document.fform.submit();
    });
});

//쿠키값 Set
function setCookie(cookieName, value, exdays){
    var exdate = new Date();
    exdate.setDate(exdate.getDate() + exdays);
    var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" +
        exdate.toGMTString());
    document.cookie = cookieName + "=" + cookieValue;
}

//쿠키값 Delete
function deleteCookie(cookieName){
    var expireDate = new Date();
    expireDate.setDate(expireDate.getDate() - 1);
    document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
}

//쿠키값 가져오기
function getCookie(cookie_name) {
    var x, y;
    var val = document.cookie.split(';');

    for (var i = 0; i < val.length; i++) {
        x = val[i].substr(0, val[i].indexOf('='));
        y = val[i].substr(val[i].indexOf('=') + 1);
        x = x.replace(/^\s+|\s+$/g, ''); // 앞과 뒤의 공백 제거하기

        if (x == cookie_name) {
            return unescape(y); // unescape로 디코딩 후 값 리턴
        }
    }
}
const secretKey = "ff5de6778574803f9006470720d0ede7dcf18f3756de29081d1aa2acae7e12d1";

$(document).ready(function() {
    var userInputId = getCookie("8777ec8809e");
    var setCookieYN = getCookie("c69f45faa12");

    if(setCookieYN === 'Y') {
        $("#saveEmail").prop("checked", true);
        const bytes = CryptoJS.AES.decrypt(userInputId, secretKey, {
            mode: CryptoJS.mode.ECB,
            padding: CryptoJS.pad.Pkcs7
        });
        $("#email").val(bytes.toString(CryptoJS.enc.Utf8));
    } else {
        $("#saveEmail").prop("checked", false);
    }

    //로그인 버튼 클릭
    $('#login_btn').click(function() {
        if($("#saveEmail").is(":checked")){

            var userInputId = CryptoJS.AES.encrypt($("#email").val(), secretKey, {
                mode: CryptoJS.mode.ECB,
                padding: CryptoJS.pad.Pkcs7
            });
            setCookie("8777ec8809e", userInputId, 60);
            setCookie("c69f45faa12", "Y", 60);
        } else {
            deleteCookie("8777ec8809e");
            deleteCookie("c69f45faa12");
        }

        document.form.submit();
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

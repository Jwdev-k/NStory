//팝업 띄우기
function openPop() {
    document.getElementById("popup_layer").style.display = "block";

}

//팝업 닫기
function closePop() {
    document.getElementById("popup_layer").style.display = "none";
}

//팝업 띄우기
function openUserInfo() {
    document.getElementById("userInfo").style.display = "block";

}

//팝업 닫기
function closeUserInfo() {
    document.getElementById("userInfo").style.display = "none";
    var input = document.getElementById("chooseImg");
    input.value = null;
}
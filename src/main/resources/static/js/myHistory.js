$(document).ready(function() {
    postsBtn();
});
function postsBtn() {
    var postbtn = document.getElementById("recent-posts-btn");
    var commentbtn = document.getElementById("recent-comments-btn");
    postbtn.className = "btn fw-bold text-primary w-100";
    commentbtn.className = "btn fw-bold text-white w-100";

    $.ajax({
        type: "GET",
        url: "/recent_post",
        dataType: "json",
        success: function(data) {
            var html = "";
            if (data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var list = data[i];
                    html += "<li class='list-group-item d-flex justify-content-between'><a class='userinfo-t-c' href='/whiteview?id=" + list.id + "'>" + list.title +
                    "</a><span>작성일 : " + list.creationDate + "</span></li>";
                }
            } else {
                html += "<li class='list-group-item d-flex justify-content-center'><span>작성한 게시물이 없습니다.</span></li>"
            }
            $("#recent-list").html(html);
        },
        error: function() {
            alert("오류가 발생했습니다.");
        }
    })
}
function commentsBtn() {
    var postbtn = document.getElementById("recent-posts-btn");
    var commentbtn = document.getElementById("recent-comments-btn");
    postbtn.className = "btn fw-bold text-white w-100";
    commentbtn.className = "btn fw-bold text-primary w-100";

    $.ajax({
        type: "GET",
        url: "/recent_comment",
        dataType: "json",
        success: function(data) {
            var html = "";
            if (data.length > 0) {
                for (var i = 0; i < data.length; i++) {
                    var list = data[i];
                    html += "<li class='list-group-item d-flex justify-content-between'><a class='userinfo-t-c' href='/whiteview?id=" + list.id + "'>" + list.contents +
                    "</a><span>작성일 : " + list.time + "</span></li>";
                }
            } else {
                html += "<li class='list-group-item d-flex justify-content-center'><span>작성한 댓글이 없습니다.</span></li>"
            }
            $("#recent-list").html(html);
        },
        error: function() {
            alert("오류가 발생했습니다.");
        }
    })
}
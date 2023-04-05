function getCookie(key){
    var result = null;
    var cookie = document.cookie.split(';');
    cookie.some(function (item) {
        item = item.replace(' ', '');

        var dic = item.split('=');

        if (key === dic[0]) {
            result = dic[1];
        }
    });
    return result
}

var co = getCookie("searchType");
var coText = null;
if (co == 1){
    coText = 'title'
} else if (co == 2){
    coText = 'contents'
} else if (co == 3){
    coText = 'author'
}
$('#search-type').ready(function () {
    $('#search-type').val(coText)
})
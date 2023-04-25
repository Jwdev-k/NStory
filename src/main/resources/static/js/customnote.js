$(document).ready(function () {
    var fontList = ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', 'Noto Sans KR', '맑은 고딕', '궁서', '굴림체', '굴림', '돋움체', '바탕체'];
    //여기 아래 부분
    $('#summernote').summernote({
        dialogsInBody: true,
        height: 600,                 // 에디터 높이
        minHeight: 600,             // 최소 높이
        maxHeight: 600,             // 최대 높이
        focus: true,                  // 에디터 로딩후 포커스를 맞출지 여부
        lang: "ko-KR",					// 한글 설정
        placeholder: '내용을 작성해주세요.',
        tabDisable: false,
        toolbar: [
            // [groupName, [list of button]]
            ['font',['fontname','fontsize']],
            ['style', ['bold','italic','underline','strikethrough','superscript','subscript','clear']],
            ['color', ['forecolor', 'backcolor']],
            ['misc', ['undo', 'redo']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert', ['picture', 'link', 'video']],
            ['view', ['codeview']],
        ],
        fontNames: fontList,
        fontNamesIgnoreCheck: fontList,
        fontSizes: ['10', '11', '12', '13', '14', '16', '18', '20', '22', '24', '28', '30', '36', '50', '72'],
        callbacks: {	//여기 부분이 이미지를 첨부하는 부분
            onImageUpload : function(files) {
                uploadSummernoteImageFile(files[0],this);
            },
            onPaste: function (e) {
                var clipboardData = e.originalEvent.clipboardData;
                if (clipboardData && clipboardData.items && clipboardData.items.length) {
                    var item = clipboardData.items[0];
                    if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
                        e.preventDefault();
                    }
                }
            },
            onInit: function() {
                $('.note-resizebar').css({
                    display: 'none'
                })
            },
            onFont: function(size) {
                // 현재 선택된 폰트색 가져오기
                var color = $('.note-color').val();
                // 선택된 폰트크기와 폰트색으로 폰트 스타일 지정
                $('#summernote').summernote('createRange').css({
                    'font-size': size + 'px',
                    'color': color,
                });
            }
        }
    });
});

function uploadSummernoteImageFile(file, editor) {
    image = new FormData();
    image.append("summernote", file);
    $.ajax({
        data : image,
        type : "POST",
        url : "/summernote/upload",
        contentType : false,
        processData : false,
        async: false,
        success : function(data) {
            //항상 업로드된 파일의 url이 있어야 한다.
            $(editor).summernote('insertImage', data);
            $('img').attr('style', 'max-width:100%; height:auto;');
        }
    });
}

$("div.note-editable").on('drop',function(e){
    for(i=0; i< e.originalEvent.dataTransfer.files.length; i++){
        uploadSummernoteImageFile(e.originalEvent.dataTransfer.files[i],$("#summernote")[0]);
    }
    e.preventDefault();
})

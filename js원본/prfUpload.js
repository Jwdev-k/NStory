$(function() {
    var cropper;

    // 이미지 파일 선택 시
    $("#chooseImg").on("change", function(e) {
        var files = e.target.files;
        var image = files[0];

        var reader = new FileReader();
        reader.onload = function(e) {
            $("#cropper-img").attr("src", e.target.result);
            // Cropper.js 인스턴스 생성
            if (cropper) {
                // 기존 인스턴스를 초기화 하기위해 추가.
                cropper.destroy();
            }

            cropper = new Cropper($("#cropper-img")[0], {
                aspectRatio: 1,
                viewMode: 3,
                zoomable: true,
                width: 200,
                height: 200,
                toggleDragModeOnDblclick: false,
                crop: function(event) {
                    // 자르기 영역이 변경되면 크기를 체크하여 자르기 모드로 전환
                    if (event.detail.width > 200 || event.detail.height > 200) {
                        cropper.setDragMode("move");
                    } else {
                        cropper.setDragMode("none");
                    }
                }
            });
        };
        reader.readAsDataURL(image);
    });

    // 업로드 버튼 클릭 시
    $("#upload-btn").on("click", function() {
        var canvas = cropper.getCroppedCanvas({
            width: 200,
            height: 200,
            imageSmoothingEnabled: false,
            imageSmoothingQuality: "high"
        });
        canvas.toBlob(function(blob) {
            var formData = new FormData();
            formData.append("profileImg", blob);
            $.ajax({
                url: "/account/prf_update",
                method: "POST",
                data: formData,
                processData: false,
                contentType: false,
                dataType: "json",
                success: function(response) {
                    if (response.result == "success") {
                        // redirect URL로 리다이렉트 수행
                        window.location.href = response.redirectUrl;
                    }
                },
                error: function() {
                    alert("이미지 업로드에 실패했습니다.");
                }
            });
        });
    });
});

// 폼체크 함수 시작
let DirectorRequest__submitDone = false;
function DirectorRequest__submit(form) {
    if (DirectorRequest__submitDone) {
        return;
    }


    if (form.file__member__0__director__attachment__1.value.length == 0) {
        swal({
            title: "장례지도사 자격증 사진을 업로드해주세요.",
            icon: "info",
            button: "확인",
        });
        form.file__member__0__director__attachment__1.focus();

        return;
    }
    // 파일의 크기 및 이미지파일이 맞는지 비동기체크
    else{
        // MB 단위의 숫자 설정
        var maxSizeMb = 50;
        // maxSizeMb 에서 받은 숫자로 계산된 실제 Bite 값
        var maxSize = maxSizeMb * 1024 * 1024;
        // 이미지파일의 확장자들
        var fileForm = /(.*?)\.(jpg|jpeg|png|gif|bmp|pdf)$/;
        if (form.file__member__0__director__attachment__1.value) {
            // input 태그에서 이미지파일만 선택할 수 있게 했지만 ie10 이하의 브라우저에선 지원되지 않기때문에 안전성을 위해 한번더 체크
            if(!form.file__member__0__director__attachment__1.value.match(fileForm)) {
            	alert("이미지 파일만 업로드 가능합니다");
                return;
            }

            if (form.file__member__0__director__attachment__1.files[0].size > maxSize) {
                alert(maxSizeMb + "MB 이하의 파일을 업로드 해주세요.");
                form.file__member__0__director__attachment__1.focus();

                return;
            }
        }
    }

    form.submit();
    DirectorRequest__submitDone = true;
}
// 폼체크 함수 끝
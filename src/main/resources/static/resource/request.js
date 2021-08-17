// 폼체크 함수 시작
let DirectorRequest__submitDone = false;
function DirectorRequest__submit(form) {
    if (DirectorRequest__submitDone) {
        return;
    }

    if (form.file__director__0__common__attachment__1.value.length == 0) {
        swal({
            title: "파일을 업로드해주세요.",
            icon: "info",
            button: "확인",
        });
        form.file__director__0__common__attachment__1.focus();

        return;
    }

    form.submit();
    DirectorRequest__submitDone = true;
}
// 폼체크 함수 끝
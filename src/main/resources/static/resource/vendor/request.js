// 폼체크 함수 시작
let VendorRequest__submitDone = false;
function VendorRequest__submit(form) {
    if (VendorRequest__submitDone) {
        return;
    }

    var fileCheck = form.file__member__0__vendor__attachment__1.value;

    if (form.file__member__0__vendor__attachment__1.value.length == 0) {
        swal({
            title: "사업자 등록증 사진을 업로드해주세요.",
            icon: "info",
            button: "확인",
        });
        form.file__member__0__vendor__attachment__1.focus();

        return;
    }

    form.submit();
    VendorRequest__submitDone = true;
}
// 폼체크 함수 끝
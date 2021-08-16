// 폼체크 함수 시작
let MemberLogin__submitDone = false;
function MemberLogin__submit(form) {
    if (MemberLogin__submitDone) {
        return;
    }

    if (form.loginId.value.length == 0) {
        swal({
            title: "아이디를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginId.focus();

        return;
    }

    if (form.loginPw.value.length == 0) {
        swal({
            title: "비밀번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginPw.focus();

        return;
    }

    form.loginPwInput.value = sha256(form.loginPw.value);
    form.loginPw.value = '';

    form.submit();
    MemberLogin__submitDone = true;
}
// 폼체크 함수 끝
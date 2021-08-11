// 폼체크 함수 시작
let MemberLogin__submitDone = false;
function MemberLogin__submit(form) {
    if (MemberLogin__submitDone) {
        return;
    }

    if (form.loginId.value.length == 0) {
        alert('아이디를 입력해주세요.');
        form.loginId.focus();

        return;
    }

    if (form.loginPw.value.length == 0) {
        alert('비밀번호를 입력해주세요.');
        form.loginPw.focus();

        return;
    }

    form.submit();
    MemberLogin__submitDone = true;
}
// 폼체크 함수 끝
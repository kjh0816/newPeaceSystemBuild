// 폼체크 함수 시작

let MemberLogin__submitDone = false;

function MemberLogin__submit(form) {


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



    var loginId = form.loginId.value;
    var loginPwInput = form.loginPwInput.value;
    var autoLogin = form.autoLogin.checked;





       const post$ = rxjs.ajax.ajax.post(
           						'/usr/member/doLogin',
           						new FormData(form)
           					);


           					post$.subscribe(
           						res => {
           							if ( res.response.success ) {
                   //                      회원가입 성공일 경우, 로그인 페이지로 replace시킨다.
                                            alert(autoLogin);
                                           window.location.replace(res.response.map.replaceUri);
           							}
           							else {
               //                                    회원가입 실패일 경우, 실패 원인 메시지를 띠워준다.
                                           alert(res.response.msg);
           							}
           						}
           					);
}
// 폼체크 함수 끝
function MemberModifyPw__submit(form) {


    form.loginPw.value = form.loginPw.value.trim();
    if (form.loginPw.value.length == 0) {
        swal({
            title: "비밀번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginPw.focus();

        return;
    }

    form.loginPwConfirm.value = form.loginPwConfirm.value.trim();
    if (form.loginPwConfirm.value.length == 0) {
        swal({
            title: "비밀번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginPwConfirm.focus();

        return;
    }

    if (form.loginPw.value != form.loginPwConfirm.value) {
        swal({
            title: "비밀번호가 서로 일치하지않습니다.",
            icon: "warning",
            button: "확인",
        });

        form.loginPw.focus();

        return;
    }


    form.loginPwInput.value = sha256(form.loginPw.value);
    form.loginPw.value = '';
    form.loginPwConfirm.value = '';



    const post$ = rxjs.ajax.ajax.post(
    						'/usr/member/doModifyPw',
    						new FormData(form)
    					);





    					post$.subscribe(
    						res => {
    							if ( res.response.success ) {
                                    alert(res.response.msg);
                                    window.location.replace('/usr/member/info');
    							}
    							else {
                                    alert(res.response.msg);
    							}
    						}
    					);


}
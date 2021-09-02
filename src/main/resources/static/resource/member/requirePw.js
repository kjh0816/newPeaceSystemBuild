function MemberRequirePw__submit(form){
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

     form.loginPwInput.value = sha256(form.loginPw.value);
     form.loginPw.value = '';



     const post$ = rxjs.ajax.ajax.post(
        						'/usr/member/doRequirePw',
        						new FormData(form)
        					);



        					post$.subscribe(
        						res => {
        							if ( res.response.success ) {
                //                      비밀번호가 일치할 경우, action 파라미터에 따라서 이동시킨다.
                                        window.location.replace('/usr/member/' + res.response.map.action);


        							}
        							else {
            //                           비밀번호가 일치 하지 않을 경우, 실패 원인 메시지를 띠워준다.

                                        alert(res.response.msg);
        							}
        						}
        					);
}
function MemberModifyInfo__submit(form){




    form.cellphoneNo.value = form.cellphoneNo.value.trim();
    if (form.cellphoneNo.value.length == 0) {
        swal({
            title: "전화번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.cellphoneNo.focus();

        return;
    }

    form.email.value = form.email.value.trim();
    if (form.email.value.length == 0) {
        swal({
            title: "이메일을 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.email.focus();

        return;
    }

    form.location.value = form.location.value.trim();
    if (form.location.value.length == 0) {
        swal({
            title: "지역을 선택해주세요.",
            icon: "info",
            button: "확인",
        });
        form.location.focus();

        return;
    }


    form.bank.value = form.bank.value.trim();
        if (form.bank.value.length == 0) {
            swal({
                title: "은행을 선택해주세요.",
                icon: "info",
                button: "확인",
            });
            form.bank.focus();

            return;
        }





    const post$ = rxjs.ajax.ajax.post(
    						'/usr/member/doModifyInfo',
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
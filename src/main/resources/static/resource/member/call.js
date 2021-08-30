function MemberCall__submit(form) {


    form.deceasedName.value = form.deceasedName.value.trim();
    if (form.deceasedName.value.length == 0) {
            swal({
                title: "고인의 성함을 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.deceasedName.focus();

            return;
    }

    form.relatedName.value = form.relatedName.value.trim();
    if (form.relatedName.value.length == 0) {
            swal({
                title: "상주의 성함을 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.relatedName.focus();

            return;
    }

    form.cellphoneNo.value = form.cellphoneNo.value.trim();
    if (form.cellphoneNo.value.length == 0) {
            swal({
                title: "상주의 연락처를 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.cellphoneNo.focus();

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

    form.address.value = form.address.value.trim();
    if (form.address.value.length == 0) {
            swal({
                title: "상세 주소를 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.address.focus();

            return;
    }

    const post$ = rxjs.ajax.ajax.post(
    //              고인의 정보를 올바르게 입력했을 경우, client 테이블에 row가 추가된다.
    //              clientId를 추가 정보로 return한다.
                    '/usr/member/doCall',
                    new FormData(form)
                );

    post$.subscribe(
        res => {
            if ( res.response.success ) {

//          doCall에서 return된 clientId를 URL 파라미터로 넘겨준다.
                const clientId = res.response.map.clientId;

                alert(res.response.msg);
                window.location.replace('/usr/member/progress?clientId=' + clientId);
            }
            else {
//              고인 정보가 제대로 입력되지 않은 경우
                alert(res.response.msg);
            }
        }
    );

}
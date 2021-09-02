let SelectFlower__submitDone = false;

function SelectFlower__submit(form) {
    if (form.flowerId.value.length == 0) {
        swal({
            title: "제단꽃을 선택해주세요.",
            icon: "info",
            button: "확인",
        });

        return;
    }


    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectFlower',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                   if ( !confirm(res.response.msg) ) return false;
                   window.location.href="/usr/director/progress?clientId="+res.response.map.funeral.clientId;
            }
            else {
                   alert(res.response.msg);
            }
        }
    );
}
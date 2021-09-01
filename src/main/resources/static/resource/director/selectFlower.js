function SelectFlower__submit(form) {
    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectFlower',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                   alert(res.response.msg);
                   window.location.href="/usr/director/progress?clientId="+res.response.map.funeral.clientId;
            }
            else {
                   alert(res.response.msg);
            }
        }
    );
}
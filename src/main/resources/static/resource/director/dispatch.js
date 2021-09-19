function Dispatch__submit(form) {
    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doDispatch',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                   alert(res.response.msg);
                   window.location.href="/usr/director/progress?clientId="+res.response.map.client.id;
            }
            else {
                   alert(res.response.msg);
                   window.location.replace(res.response.map.replaceUrl);
            }
        }
    );
}
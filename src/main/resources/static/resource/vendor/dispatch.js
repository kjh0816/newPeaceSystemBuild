function Dispatch__submit(form) {
    const post$ = rxjs.ajax.ajax.post(
        '/usr/vendor/doDispatch',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                   alert(res.response.msg);
                   window.location.href="/usr/vendor/order";
            }
            else {
                   alert(res.response.msg);
            }
        }
    );
}
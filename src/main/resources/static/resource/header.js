function DirectorMoveProgress__submit(form) {
    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/moveProgress',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                   window.location.href="/usr/director/progress";
            }
            else {
                   alert(res.response.msg);
            }
        }
    );
}
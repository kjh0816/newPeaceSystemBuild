function pagination(form){
    const post$ = rxjs.ajax.ajax.post(
        '/adm/home/getPage',
        new FormData(form)
    );


    post$.subscribe(
        res => {
            if ( res.response.success ) {
                window.location.replace(res.response.map.replaceUri);
            }
            else{
                alert(res.response.msg);
            }
        }
    );
}
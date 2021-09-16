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

function getParam(sname) {
    var params = location.search.substr(location.search.indexOf("?") + 1);
    var sval = "";
    params = params.split("&");

    for (var i = 0; i < params.length; i++) {

        temp = params[i].split("=");

        if ([temp[0]] == sname) { sval = temp[1]; }

    }

    return sval;
}
$("input[value=" + getParam('page') + "]").css('color', 'black');
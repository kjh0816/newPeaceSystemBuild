function Complete__submit(form) {
    const post$ = rxjs.ajax.ajax.post(
        '/usr/vendor/doComplete',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                window.location.href="/usr/vendor/order";
            }
            else {
                alert(res.response.msg);
            }
        }
    );
}

$('#flowerOrdersBtn').click(function(){
    $('#flowerOrders').css('display', 'block');
    $('#flowerTributeOrders').css('display', 'none');
});
$('#flowerTributeOrdersBtn').click(function(){
    $('#flowerTributeOrders').css('display', 'block');
    $('#flowerOrders').css('display', 'none');
});
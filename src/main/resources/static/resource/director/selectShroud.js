let Selectshroud__submitDone = false;

function Selectshroud__submit(form) {
    if (form.shroudId.value.length == 0 && form.shroudTributeId.value.length == 0) {
        swal({
            title: "수의를 선택해주세요.",
            icon: "info",
            button: "확인",
        });

        return;
    }


    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectShroud',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                   if ( !confirm(res.response.msg) ) return false;
                   window.location.href="/usr/director/progress";
            }
            else {
                   alert(res.response.msg);
            }
        }
    );
}
// radio 버튼 대신 사용할 버튼 디자인
$('.shroudIdSelectBox').click(function () {
    var shroudId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.shroudIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + shroudId + "']#shroudId").prop('checked', false);
    }
    else{
        $('.shroudIdSelectBox').removeClass('active');
        $('.shroudIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + shroudId + "']#shroudId").prop('checked', true);
    }
});
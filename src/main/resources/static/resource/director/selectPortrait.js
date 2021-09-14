let SelectPortrait__submitDone = false;

function SelectPortrait__submit(form) {
    if (form.portraitId.value.length == 0) {
        swal({
            title: "영정액자를 선택해주세요.",
            icon: "info",
            button: "확인",
        });

        return;
    }

    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectPortrait',
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

$('.portraitIdSelectBox').click(function () {
    var portraitId = $(this).attr('id');

    if($(this).hasClass('active')){
        $('.portraitIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + portraitId + "']").prop('checked', false);
    }
    else{
        $('.portraitIdSelectBox').removeClass('active');
        $('.portraitIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택완료");
        $("input[value='" + portraitId + "']").prop('checked', true);
    }
});
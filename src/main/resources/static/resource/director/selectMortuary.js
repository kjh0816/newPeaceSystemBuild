let SelectMortuary__submitDone = false;

function selectMortuary__submit(form) {
    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectMortuary',
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
$('.incenseIdSelectBox').click(function () {
    var incenseId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.incenseIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + incenseId + "']#incenseId").prop('checked', false);
        $("input[name='incenseCnt']").val('');
        $("#incenseCnt" + incenseId).css("display", "none");
    }
    else{
        $('.incenseIdSelectBox').removeClass('active');
        $('.incenseIdSelectBox').text('선택하기');
        $("input[name='incenseCnt']").css("display", "none");
        $("input[name='incenseCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + incenseId + "']#incenseId").prop('checked', true);
        $("#incenseCnt" + incenseId).css("display", "block");
    }
});
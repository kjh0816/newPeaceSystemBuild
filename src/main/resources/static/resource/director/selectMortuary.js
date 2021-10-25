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

$('.candleIdSelectBox').click(function () {
    var candleId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.candleIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + candleId + "']#candleId").prop('checked', false);
        $("input[name='candleCnt']").val('');
        $("#candleCnt" + candleId).css("display", "none");
    }
    else{
        $('.candleIdSelectBox').removeClass('active');
        $('.candleIdSelectBox').text('선택하기');
        $("input[name='candleCnt']").css("display", "none");
        $("input[name='candleCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + candleId + "']#candleId").prop('checked', true);
        $("#candleCnt" + candleId).css("display", "block");
    }
});

$('.ancestralTabletIdSelectBox').click(function () {
    var ancestralTabletId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.ancestralTabletIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + ancestralTabletId + "']#ancestralTabletId").prop('checked', false);
        $("input[name='ancestralTabletCnt']").val('');
        $("#ancestralTabletCnt" + ancestralTabletId).css("display", "none");
    }
    else{
        $('.ancestralTabletIdSelectBox').removeClass('active');
        $('.ancestralTabletIdSelectBox').text('선택하기');
        $("input[name='ancestralTabletCnt']").css("display", "none");
        $("input[name='ancestralTabletCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + ancestralTabletId + "']#ancestralTabletId").prop('checked', true);
        $("#ancestralTabletCnt" + ancestralTabletId).css("display", "block");
    }
});

$('.condolenceMoneyBookIdSelectBox').click(function () {
    var condolenceMoneyBookId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.condolenceMoneyBookIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + condolenceMoneyBookId + "']#condolenceMoneyBookId").prop('checked', false);
        $("input[name='condolenceMoneyBookCnt']").val('');
        $("#condolenceMoneyBookCnt" + condolenceMoneyBookId).css("display", "none");
    }
    else{
        $('.condolenceMoneyBookIdSelectBox').removeClass('active');
        $('.condolenceMoneyBookIdSelectBox').text('선택하기');
        $("input[name='condolenceMoneyBookCnt']").css("display", "none");
        $("input[name='condolenceMoneyBookCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + condolenceMoneyBookId + "']#condolenceMoneyBookId").prop('checked', true);
        $("#condolenceMoneyBookCnt" + condolenceMoneyBookId).css("display", "block");
    }
});

$('.condolenceBookIdSelectBox').click(function () {
    var condolenceBookId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.condolenceBookIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + condolenceBookId + "']#condolenceBookId").prop('checked', false);
        $("input[name='condolenceBookCnt']").val('');
        $("#condolenceBookCnt" + condolenceBookId).css("display", "none");
    }
    else{
        $('.condolenceBookIdSelectBox').removeClass('active');
        $('.condolenceBookIdSelectBox').text('선택하기');
        $("input[name='condolenceBookCnt']").css("display", "none");
        $("input[name='condolenceBookCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + condolenceBookId + "']#condolenceBookId").prop('checked', true);
        $("#condolenceBookCnt" + condolenceBookId).css("display", "block");
    }
});

$('.pictureRibbonIdSelectBox').click(function () {
    var pictureRibbonId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.pictureRibbonIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + pictureRibbonId + "']#pictureRibbonId").prop('checked', false);
        $("input[name='pictureRibbonCnt']").val('');
        $("#pictureRibbonCnt" + pictureRibbonId).css("display", "none");
    }
    else{
        $('.pictureRibbonIdSelectBox').removeClass('active');
        $('.pictureRibbonIdSelectBox').text('선택하기');
        $("input[name='pictureRibbonCnt']").css("display", "none");
        $("input[name='pictureRibbonCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + pictureRibbonId + "']#pictureRibbonId").prop('checked', true);
        $("#pictureRibbonCnt" + pictureRibbonId).css("display", "block");
    }
});
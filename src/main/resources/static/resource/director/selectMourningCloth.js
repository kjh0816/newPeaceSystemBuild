let SelectMourningCloth__submitDone = false;

function SelectMourningCloth__submit(form) {
    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectMourningCloth',
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
$('.femaleMourningClothBlackIdSelectBox').click(function () {
    var femaleMourningClothBlackId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.femaleMourningClothBlackIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + femaleMourningClothBlackId + "']#femaleMourningClothBlackId").prop('checked', false);
        $("input[name='femaleClothCnt']").val('');
        $("#femaleMourningClothBlackCnt" + femaleMourningClothBlackId).css("display", "none");
    }
    else{
        $('.femaleMourningClothBlackIdSelectBox').removeClass('active');
        $('.femaleMourningClothBlackIdSelectBox').text('선택하기');
        $("input[name='femaleClothCnt']").css("display", "none");
        $("input[name='femaleClothCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + femaleMourningClothBlackId + "']#femaleMourningClothBlackId").prop('checked', true);
        $("#femaleMourningClothBlackCnt" + femaleMourningClothBlackId).css("display", "block");
    }
});
$('.femaleMourningClothWhiteIdSelectBox').click(function () {
    var femaleMourningClothWhiteId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.femaleMourningClothWhiteIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + femaleMourningClothWhiteId + "']#femaleMourningClothWhiteId").prop('checked', false);
        $("input[name='femaleClothCnt']").val('');
        $("#femaleMourningClothWhiteCnt" + femaleMourningClothWhiteId).css("display", "none");
    }
    else{
        $('.femaleMourningClothWhiteIdSelectBox').removeClass('active');
        $('.femaleMourningClothWhiteIdSelectBox').text('선택하기');
        $("input[name='femaleClothCnt']").css("display", "none");
        $("input[name='femaleClothCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + femaleMourningClothWhiteId + "']#femaleMourningClothWhiteId").prop('checked', true);
        $("#femaleMourningClothWhiteCnt" + femaleMourningClothWhiteId).css("display", "block");
    }
});
$('.maleMourningClothIdSelectBox').click(function () {
    var maleMourningClothId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.maleMourningClothIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + maleMourningClothId + "']#maleMourningClothId").prop('checked', false);
        $("input[name='maleClothCnt']").val('');
        $("#maleClothCnt" + maleMourningClothId).css("display", "none");
    }
    else{
        $('.maleMourningClothIdSelectBox').removeClass('active');
        $('.maleMourningClothIdSelectBox').text('선택하기');
        $("input[name='maleClothCnt']").css("display", "none");
        $("input[name='maleClothCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + maleMourningClothId + "']#maleMourningClothId").prop('checked', true);
        $("#maleClothCnt" + maleMourningClothId).css("display", "block");
    }
});
$('.shirtIdSelectBox').click(function () {
    var shirtId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.shirtIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + shirtId + "']#shirtId").prop('checked', false);
        $("input[name='shirtCnt']").val('');
        $("#shirtCnt"+ shirtId).css("display", "none");
    }
    else{
        $('.shirtIdSelectBox').removeClass('active');
        $('.shirtIdSelectBox').text('선택하기');
        $("input[name='shirtCnt']").css("display", "none");
        $("input[name='shirtCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + shirtId + "']#shirtId").prop('checked', true);
        $("#shirtCnt" + shirtId).css("display", "block");
    }
});

$('.necktieIdSelectBox').click(function () {
    var necktieId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.necktieIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + necktieId + "']#necktieId").prop('checked', false);
        $("input[name='necktieCnt']").val('');
        $("#necktieCnt" + necktieId).css("display", "none");
    }
    else{
        $('.necktieIdSelectBox').removeClass('active');
        $('.necktieIdSelectBox').text('선택하기');
        $("input[name='necktieCnt']").css("display", "none");
        $("input[name='necktieCnt']").val('');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + necktieId + "']#necktieId").prop('checked', true);
        $("#necktieCnt"+ necktieId).css("display", "block");
    }
});

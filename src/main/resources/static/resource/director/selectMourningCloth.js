let SelectMourningCloth__submitDone = false;

function SelectMourningCloth__submit(form) {
    if (form.femaleMourningClothId.value.length == 0) {
        swal({
            title: "아무것도 선택되지 않았습니다.",
            icon: "info",
            button: "확인",
        });

        return;
    }
    else {
        if(form.femaleClothCnt.value.length == 0){
            swal({
                title: "주문할 상복(여)의 갯수를 입력해주세요.",
                icon: "info",
                button: "확인",
            });

            return;
        }
    }


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
$('.femaleMourningClothIdSelectBox').click(function () {
    var femaleMourningClothId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.femaleMourningClothIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + femaleMourningClothId + "']#femaleMourningClothId").prop('checked', false);
        $("input[name='femaleClothCnt']").val('');
    }
    else{
        $('.femaleMourningClothIdSelectBox').removeClass('active');
        $('.femaleMourningClothIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + femaleMourningClothId + "']#femaleMourningClothId").prop('checked', true);
    }
});
$('.maleMourningClothIdSelectBox').click(function () {
    var maleMourningClothId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.maleMourningClothIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + maleMourningClothId + "']#maleMourningClothId").prop('checked', false);
        $("input[name='maleClothCnt']").val('');
    }
    else{
        $('.maleMourningClothIdSelectBox').removeClass('active');
        $('.maleMourningClothIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + maleMourningClothId + "']#maleMourningClothId").prop('checked', true);
    }
});
$('.shirtIdSelectBox').click(function () {
    var shirtId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.shirtIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + shirtId + "']#shirtId").prop('checked', false);
        $("input[name='shirtCnt']").val('');
    }
    else{
        $('.shirtIdSelectBox').removeClass('active');
        $('.shirtIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + shirtId + "']#shirtId").prop('checked', true);
    }
});

$('.necktieIdSelectBox').click(function () {
    var necktieId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.necktieIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + necktieId + "']#necktieId").prop('checked', false);
        $("input[name='necktieCnt']").val('');
    }
    else{
        $('.necktieIdSelectBox').removeClass('active');
        $('.necktieIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + necktieId + "']#necktieId").prop('checked', true);
    }
});
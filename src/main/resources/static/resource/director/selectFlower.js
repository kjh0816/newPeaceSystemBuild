let SelectFlower__submitDone = false;

function SelectFlower__submit(form) {
    if (form.flowerId.value.length == 0 && form.flowerTributeId.value.length == 0) {
        swal({
            title: "제단꽃, 헌화 둘중 하나는 선택하셔야 주문이 가능합니다.",
            icon: "info",
            button: "확인",
        });

        return;
    }


    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectFlower',
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
$('.flowerIdSelectBox').click(function () {
    var flowerId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.flowerIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + flowerId + "']#flowerId").prop('checked', false);
    }
    else{
        $('.flowerIdSelectBox').removeClass('active');
        $('.flowerIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + flowerId + "']#flowerId").prop('checked', true);
    }
});
$('.flowerTributeIdSelectBox').click(function () {
    var flowerTributeId = $(this).attr('value');

    if($(this).hasClass('active')){
        $('.flowerTributeIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + flowerTributeId + "']#flowerTributeId").prop('checked', false);
        $("input[name='bunchCnt']").val('');
        $("#bunchCnt").css("display", "none");
    }
    else{
        $('.flowerTributeIdSelectBox').removeClass('active');
        $('.flowerTributeIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택 됨");
        $("input[value='" + flowerTributeId + "']#flowerTributeId").prop('checked', true);
        $("#bunchCnt").css("display", "block");
    }
});
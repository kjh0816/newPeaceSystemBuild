let SelectFlower__submitDone = false;

function SelectFlower__submit(form) {
    if (form.flowerId.value.length == 0) {
        swal({
            title: "제단꽃을 선택해주세요.",
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
function SelectFlowerTribute__submit(form) {
    if (form.flowerTributeId.value.length == 0) {
        swal({
            title: "헌화를 선택해주세요.",
            icon: "info",
            button: "확인",
        });

        return;
    }

    if (form.bunchCnt.value.length == 0) {
        swal({
            title: "주문하실 묶음갯수를 입력해주세요.",
            icon: "info",
            button: "확인",
        });

        return;
    }


    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectFlowerTribute',
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
    var flowerId = $(this).attr('id');

    if($(this).hasClass('active')){
        $('.flowerIdSelectBox').text('선택하기');
        $(this).removeClass('active');
        $("input[value='" + flowerId + "']").prop('checked', false);
    }
    else{
        $('.flowerIdSelectBox').removeClass('active');
        $('.flowerIdSelectBox').text('선택하기');
        $(this).addClass('active');
        $(this).text("선택완료");
        $("input[value='" + flowerId + "']").prop('checked', true);
    }
});
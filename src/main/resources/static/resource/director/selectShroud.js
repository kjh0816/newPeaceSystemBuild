let SelectShroud__submitDone = false;

function SelectShroud__submit(form) {
    if (form.shroudId.value.length == 0) {
        swal({
            title: "수의를 선택해주세요.",
            icon: "info",
            button: "확인",
        });

        return;
    }

    if (form.coffinId.value.length == 0) {
            swal({
                title: "관을 선택해주세요.",
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


$('#coffinName').change(function(){

    var coffinName = $('#coffinName').val().trim().toString();


        $.ajax({
            type: 'POST',
            url: './coffinChi',
            dataType: 'json',
            data: {coffinName:coffinName},
            success: function(result){

                // 1) SELECT BOX의 기존 option들을 지운다.
                $('#coffinChi').empty();
                // 2) SELECT BOX의 제목을 넣어준다. (선택 불가 option)
                $('#coffinChi').append("<option disabled selected>치</option>")
                // 3) SELECT BOX에 option을 추가한다.
                for(var count = 0; count < result.map.coffinChis.length; count++){
                    var option = $("<option>" + result.map.coffinChis[count] + "</option>");
                    $('#coffinChi').append(option);
                }

            }
        });
});

$('#coffinChi').change(function(){

    var coffinChi = $('#coffinChi').val().trim().toString();


        $.ajax({
            type: 'POST',
            url: './coffinSize',
            dataType: 'json',
            data: {coffinChi:coffinChi},
            success: function(result){

                // 1) SELECT BOX의 기존 option들을 지운다.
                $('#coffinSize').empty();
                // 2) SELECT BOX의 제목을 넣어준다. (선택 불가 option)
                $('#coffinSize').append("<option disabled selected>사이즈</option>")
                // 3) SELECT BOX에 option을 추가한다.
                for(var count = 0; count < result.map.coffinSizes.length; count++){
                    var option = $("<option>" + result.map.coffinSizes[count] + "</option>");
                    $('#coffinSize').append(option);
                }

            }
        });
});

$('#coffinSize').change(function(){

    var coffinName = $('#coffinName').val().trim().toString();
    var coffinChi = $('#coffinChi').val().trim().toString();
    var coffinSize = $('#coffinSize').val().trim().toString();


        $.ajax({
            type: 'POST',
            url: './getCoffinId',
            dataType: 'json',
            data: {
            coffinName:coffinName,
            coffinChi:coffinChi,
            coffinSize:coffinSize
            },
            success: function(result){

                $('#coffinId').val(result.map.coffinId);

            }
        });
});
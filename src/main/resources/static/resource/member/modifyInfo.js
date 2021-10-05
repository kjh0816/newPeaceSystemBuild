// 폼 체크 (시작)
function MemberModifyInfo__submit(form){




    form.cellphoneNo.value = form.cellphoneNo.value.trim();
    if (form.cellphoneNo.value.length == 0) {
        swal({
            title: "전화번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.cellphoneNo.focus();

        return;
    }

    form.email.value = form.email.value.trim();
    if (form.email.value.length == 0) {
        swal({
            title: "이메일을 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.email.focus();

        return;
    }

    form.location.value = form.location.value.trim();
    if (form.location.value.length == 0) {
        swal({
            title: "지역을 선택해주세요.",
            icon: "info",
            button: "확인",
        });
        form.location.focus();

        return;
    }


    form.bank.value = form.bank.value.trim();
        if (form.bank.value.length == 0) {
            swal({
                title: "은행을 선택해주세요.",
                icon: "info",
                button: "확인",
            });
            form.bank.focus();

            return;
        }





    const post$ = rxjs.ajax.ajax.post(
    						'/usr/member/doModifyInfo',
    						new FormData(form)
    					);


    					post$.subscribe(
    						res => {
    							if ( res.response.success ) {
                                    alert(res.response.msg);
                                    window.location.replace('/usr/member/info');
    							}
    							else {
                                    alert(res.response.msg);
    							}
    						}
    					);
}


// 폼 체크 (끝)

// 입력 양식 검사 (시작)
$('#email').blur(function(){
    let email = $('#email').val();
    $("#modifyInfo-submit").val() + 'email';
//   (1) 입력 여부 검사
    if(email.length == 0){
                    $("#emailCheckMsg").html('이메일을 입력해주세요.');
                    $("#emailCheckMsg").css("color", "red");
                    $("#modifyInfo-submit").attr("disabled", true);
                    return;
                }
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: './emailCheck',
        data: {email:email},
        success: function(result){
//   (2) ResultData에 따른 결과값 표시
            // 기존 이메일인 경우
            if(result.resultCode.startsWith('S-') && email == result.map.email){
                $("#emailCheckMsg").html(result.msg);
                $("#emailCheckMsg").css("color", "green");
                $("#modifyInfo-submit").attr("disabled", false);
                return;
            }
            // 기존 이메일이 아니고, 이미 가입된 이메일인 경우
            if(result.resultCode.startsWith('F-') && email != result.map.email){
                            $("#emailCheckMsg").html(result.msg);
                            $("#emailCheckMsg").css("color", "red");
                            $("#modifyInfo-submit").attr("disabled", true);
                            return;
                        }
            if(result.resultCode.startsWith('S-')){
                $("#emailCheckMsg").html(result.msg);
                $("#emailCheckMsg").css("color", "green");
                $("#modifyInfo-submit").attr("disabled", false);
                return;
            }
        }
    });
});

$('#cellphoneNo').blur(function(){
    var cellphoneNo = $("#cellphoneNo").val();
//  (1) 입력 여부 검사
    if(cellphoneNo.length == 0){
        $("#cellphoneNoCheckMsg").html("핸드폰 번호를 입력해주세요.");
        $("#cellphoneNoCheckMsg").css("color", "red");
        $("#modifyInfo-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (시작)
    var regex = /^[0-9]{10,11}$/;
    if(!regex.test(cellphoneNo)){
        $("#cellphoneNoCheckMsg").html('핸드폰 번호가 올바른지 확인해주세요.');
        $("#cellphoneNoCheckMsg").css("color", "red");
        $("#modifyInfo-submit").attr("disabled", true);
        return;
    }

        $("#cellphoneNoCheckMsg").html("핸드폰 번호 입력 완료.");
        $("#cellphoneNoCheckMsg").css("color", "green");
        $("#modifyInfo-submit").attr("disabled", false);
        return;
});


$('#accountNum').blur(function(){
    var accountNum = $("#accountNum").val();
    if(accountNum.length == 0){
        $("#accountNumCheckMsg").html("계좌번호를 입력해주세요.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#modifyInfo-submit").attr("disabled", true);
        return;
    }
    if(isNaN (accountNum)){
        $("#accountNumCheckMsg").html("계좌번호는 숫자만 입력해주세요.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#modifyInfo-submit").attr("disabled", true);
        return;
//       계좌번호 길이는 11 ~ 16자리
    }
    if(accountNum.length < 11 || accountNum.length > 16){
        $("#accountNumCheckMsg").html("계좌번호가 올바른지 확인해주세요.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#modifyInfo-submit").attr("disabled", true);
        return;
    }
        $("#accountNumCheckMsg").html("계좌번호 입력 완료.");
        $("#accountNumCheckMsg").css("color", "green");
        $("#modifyInfo-submit").attr("disabled", false);
        return;
});


// 입력 양식 검사 (시작)
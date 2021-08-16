// 폼체크 함수 시작
// blur 함수를 사용했을 때, 문제가 되는 경우는 입력을 아예하지 않으면서 피해가는 경우인데,
// 아래 함수를 통해 입력값이 아예 없는 경우를 대비할 수 있다.
let MemberJoin__submitDone = false;
function MemberJoin__submit(form) {
    if (MemberJoin__submitDone) {
        return;
    }

    form.loginId.value = form.loginId.value.trim();
    if (form.loginId.value.length == 0) {
        swal({
            title: "아이디를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginId.focus();

        return;
    }

    form.loginPw.value = form.loginPw.value.trim();
    if (form.loginPw.value.length == 0) {
        swal({
            title: "비밀번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginPw.focus();

        return;
    }

    form.loginPwConfirm.value = form.loginPwConfirm.value.trim();
    if (form.loginPwConfirm.value.length == 0) {
        swal({
            title: "비밀번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginPwConfirm.focus();

        return;
    }

    if (form.loginPw.value != form.loginPwConfirm.value) {
        swal({
            title: "비밀번호가 서로 일치하지않습니다.",
            icon: "warning",
            button: "확인",
        });

        form.loginPw.focus();

        return;
    }

    form.name.value = form.name.value.trim();
    if (form.name.value.length == 0) {
        swal({
            title: "이름을 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.name.focus();

        return;
    }

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

    form.submit();
    MemberJoin__submitDone = true;
}
// 폼체크 함수 끝

// AJax 요청으로 회원정보 체크 시작
// blur 이벤트 : focus가 해제되었을 때 실행.
// 현재 입력란에 값이 입력이 됐는지 검사해서 미입력 시, 사용자에게 입력 요청.
// 제출 전, 아이디의 현재 입력값에 대한 검사 - 3단계 (시작)
$('#loginId').blur(function(){

    var loginId = $("#loginId").val();
    var loginIdCheckMsg = $("#loginIdCheckMsg").val();
    $("#join-submit").val() + 'loginId';
//   (1) 입력 여부 검사
    if(loginId.length == 0){
        $("#loginIdCheckMsg").html("아이디를 입력해주세요.");
        $("#loginIdCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
        return;
        }

            $.ajax({
                type: 'POST',
                url: './loginIdCheck',
                dataType: 'json',
                data: {loginId:loginId},
                success: function(result){
                //   (2) ResultData에 따른 결과값 표시
                    if(result.resultCode.startsWith("F-")){
                        $("#loginIdCheckMsg").html(result.msg);
                        $("#loginIdCheckMsg").css("color", "red");
                        $("#join-submit").attr("disabled", true);
                        return;
                    }
                    if(result.resultCode.startsWith("S-")){
                        $("#loginIdCheckMsg").html(result.msg);
                        $("#loginIdCheckMsg").css("color", "green");
                        $("#join-submit").attr("disabled", false);
                        return;
                    }
                }
            });
});


// 제출 전, 아이디의 현재 입력값에 대한 검사 (끝)



$('#email').blur(function(){
    var email = $('#email').val();
    $("#join-submit").val() + 'email';
//   (1) 입력 여부 검사
    if(email.length == 0){
                    $("#emailCheckMsg").html('이메일을 입력해주세요.');
                    $("#emailCheckMsg").css("color", "red");
                    $("#join-submit").attr("disabled", true);
                    return;
                }
    $.ajax({
        type: 'POST',
        dataType: 'json',
        url: './emailCheck',
        data: {email:email},
        success: function(result){
//   (2) ResultData에 따른 결과값 표시
            if(result.resultCode.startsWith('F-')){
                $("#emailCheckMsg").html(result.msg);
                $("#emailCheckMsg").css("color", "red");
                $("#join-submit").attr("disabled", true);
                return;
            }
            if(result.resultCode.startsWith('S-')){
                $("#emailCheckMsg").html(result.msg);
                $("#emailCheckMsg").css("color", "green");
                $("#join-submit").attr("disabled", false);
                return;
            }
        }
    });
});
// 비밀번호 중복체크는 데이터베이스에있는 정보랑 비교하는것이 아니고,
// 사용자가 방금 입력한 비밀번호랑 비교하는것이기때문에 AJax 요청을 보낼 필요가 없다.
// 하지만 비동기 처리방식이기 때문에 이쪽 카테고리에 들어가있는것이다.

$('#loginPw').blur(function(){
    var loginPw = $("#loginPw").val();
    var loginPwConfirm = $("#loginPwConfirm").val();
//  (1) 입력 여부 검사
    if(loginPw.length == 0){
        $("#loginPwCheckMsg").html("비밀번호를 한 번 더 입력해주세요.");
        $("#loginPwCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (시작)
    var regex1 = /^[a-zA-Z0-9]{8,16}$/;

    if(!regex1.test(loginPw)){
        $("#loginPwCheckMsg").html('8~16자의 영대소문자 조합만 가능합니다.');
        $("#loginPwCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
        return;
    }

//  (2) 입력값에 대한 정규표현식 적용 (끝)

        $("#loginPwCheckMsg").html("사용 가능한 비밀번호입니다.");
        $("#loginPwCheckMsg").css("color", "green");
        $("#join-submit").attr("disabled", false);
        return;


});


$('#loginPwConfirm').blur(function(){
    var loginPw = $("#loginPw").val();
    var loginPwConfirm = $("#loginPwConfirm").val();
    if(loginPwConfirm.length == 0){
        $("#loginPwConfirmCheckMsg").html("비밀번호를 한 번 더 입력해주세요.");
        $("#loginPwConfirmCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
        return;
    }
    var regex1 = /^[a-zA-Z0-9]{8,16}$/;
    if(!regex1.test(loginPwConfirm)){
            $("#loginPwConfirmCheckMsg").html("8~16자의 영대소문자 조합만 가능합니다.");
            $("#loginPwConfirmCheckMsg").css("color", "red");
            $("#join-submit").attr("disabled", true);
            return;
    }
        if(loginPw != loginPwConfirm){
            $("#loginPwConfirmCheckMsg").html("비밀번호가 서로 일치하지 않습니다.");
            $("#loginPwConfirmCheckMsg").css("color", "red");
            $("#join-submit").attr("disabled", true);
            return;
        }

            $("#loginPwConfirmCheckMsg").html("비밀번호가 서로 일치합니다.");
            $("#loginPwConfirmCheckMsg").css("color", "green");
            $("#join-submit").attr("disabled", false);
            return;




});
// 입력받은 계좌번호가 숫자인지 아닌지 판단해주는 함수.
// isNaN : 주어지는 값이 문자열 타입이던, 숫자 타입이던 실행된다
// 숫자가 입력될 경우 false를 리턴, 문자열이 입력도리 경우 true를 리턴해준다.
$('#accountNum').blur(function(){
    var accountNum = $("#accountNum").val();
    if(accountNum.length == 0){
        $("#accountNumCheckMsg").html("계좌번호를 입력해주세요.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
        return;
    }
    if(isNaN (accountNum)){
        $("#accountNumCheckMsg").html("계좌번호는 숫자만 입력해주세요.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
        return;
//       계좌번호 길이는 11 ~ 16자리
    }
    if(accountNum.length < 11 || accountNum.length > 16){
        $("#accountNumCheckMsg").html("계좌번호가 올바른지 확인해주세요.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
        return;
    }
        $("#accountNumCheckMsg").html("계좌번호 입력 완료.");
        $("#accountNumCheckMsg").css("color", "green");
        $("#join-submit").attr("disabled", false);
        return;
});
// AJax 요청으로 회원정보 체크 끝
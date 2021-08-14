// 폼체크 함수 시작
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
$('#loginId').blur(function(){
    var loginId = $('#loginId').val();
    $("#join-submit").val() + 'loginId';
    $.ajax({
        type: 'POST',
        url: './loginIdCheck',
        data: {loginId:loginId},
        success: function(result){
            if(result == ''){
                if(loginId.length == 0){
                    $("#loginIdCheckMsg").html('아이디를 입력해주세요.');
                    $("#loginIdCheckMsg").css("color", "red");
                    $("#join-submit").attr("disabled", true);
                    return;
                }
                else{
                    $("#loginIdCheckMsg").html('사용할 수 있는 아이디입니다.');
                    $("#loginIdCheckMsg").css("color", "green");
                    $("#join-submit").attr("disabled", false);
                    return;
                }
            }
            else{
                $("#loginIdCheckMsg").html('사용할 수 없는 아이디입니다.');
                $("#loginIdCheckMsg").css("color", "red");
                $("#join-submit").attr("disabled", true);
                return;
            }
        }
    });
});
$('#email').blur(function(){
    var email = $('#email').val();
    $("#join-submit").val() + 'email';
    $.ajax({
        type: 'POST',
        url: './emailCheck',
        data: {email:email},
        success: function(result){
            if(email.length == 0){
                $("#emailCheckMsg").html('이메일을 입력해주세요.');
                $("#emailCheckMsg").css("color", "red");
                $("#join-submit").attr("disabled", true);
                return;
            }
            if(result.startsWith('false')){
                $("#emailCheckMsg").html('이메일 형식에 맞게 입력해주세요.');
                $("#emailCheckMsg").css("color", "red");
                $("#join-submit").attr("disabled", true);
                return;
            }
            else if(result == ''){
                $("#emailCheckMsg").html('사용할 수 있는 이메일입니다.');
                $("#emailCheckMsg").css("color", "green");
                $("#join-submit").attr("disabled", false);
                return;
            }
            else{
                $("#emailCheckMsg").html('사용할 수 없는 이메일입니다.');
                $("#emailCheckMsg").css("color", "red");
                $("#join-submit").attr("disabled", true);
                return;
            }
        }
    });
});
// 비밀번호 중복체크는 데이터베이스에있는 정보랑 비교하는것이 아니고,
// 사용자가 방금 입력한 비밀번호랑 비교하는것이기때문에 AJax 요청을 보낼 필요가 없다.
// 하지만 비동기 처리방식이기 때문에 이쪽 카테고리에 들어가있는것이다.



$('#loginPwConfirm').blur(function(){
    var loginPw = $("#loginPw").val();
    var loginPwConfirm = $("#loginPwConfirm").val();
    if(loginPwConfirm.length == 0){
        $("#loginPwConfirmCheckMsg").html("비밀번호를 한 번 더 입력해주세요.");
        $("#loginPwConfirmCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
    }else{
        if(loginPw != loginPwConfirm){
            $("#loginPwConfirmCheckMsg").html("비밀번호가 서로 일치하지 않습니다.");
            $("#loginPwConfirmCheckMsg").css("color", "red");
            $("#join-submit").attr("disabled", true);
        }else{
            $("#loginPwConfirmCheckMsg").html("비밀번호가 서로 일치합니다.");
            $("#loginPwConfirmCheckMsg").css("color", "green");
            $("#join-submit").attr("disabled", false);
        }
    }
});
// 입력받은 계좌번호가 숫자인지 아닌지 판단해주는 함수.
// isNaN : 주어지는 값이 문자열 타입이던, 숫자 타입이던 실행된다
// 숫자가 입력될 경우 false를 리턴, 문자열이 입력도리 경우 true를 리턴해준다.
$('#accountNum').blur(function(){
    var accountNum = $("#accountNum").val();
    if(isNaN (accountNum)){
        $("#accountNumCheckMsg").html("계좌번호는 숫자만 입력해주세요.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
    }else if(accountNum.length < 11){
        $("#accountNumCheckMsg").html("계좌번호가 올바른지 확인해주십시오.");
        $("#accountNumCheckMsg").css("color", "red");
        $("#join-submit").attr("disabled", true);
    }else{
        $("#accountNumCheckMsg").html("계좌번호 입력 완료.");
        $("#accountNumCheckMsg").css("color", "green");
        $("#join-submit").attr("disabled", false);
    }
});
// AJax 요청으로 회원정보 체크 끝
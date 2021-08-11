// 폼체크 함수 시작
let MemberJoin__submitDone = false;
function MemberJoin__submit(form) {
    if (MemberJoin__submitDone) {
        return;
    }

    form.roleLevel.value = form.roleLevel.value.trim();
    if (form.roleLevel.value == 0) {
        alert('직업을 선택해주세요.');
        form.roleLevel.focus();

        return;
    }

    form.loginId.value = form.loginId.value.trim();
    if (form.loginId.value.length == 0) {
        alert('아이디를 입력해주세요.');
        form.loginId.focus();

        return;
    }

    form.loginPw.value = form.loginPw.value.trim();
    if (form.loginPw.value.length == 0) {
        alert('비밀번호를 입력해주세요.');
        form.loginPw.focus();

        return;
    }

    form.loginPwCheck.value = form.loginPwCheck.value.trim();
    if (form.loginPwCheck.value.length == 0) {
        alert('비밀번호를 확인해 주세요.');
        form.loginPwCheck.focus();

        return;
    }

    if (form.loginPw.value != form.loginPwCheck.value) {
        alert('비밀번호가 서로 일치하지 않습니다.');
        form.loginPwCheck.focus();

        return;
    }

    form.name.value = form.name.value.trim();
    if (form.name.value.length == 0) {
        alert('이름을 입력해주세요.');
        form.name.focus();

        return;
    }

    form.cellphoneNo.value = form.cellphoneNo.value.trim();
    if (form.cellphoneNo.value.length == 0) {
        alert('전화번호를 입력해주세요.');
        form.cellphoneNo.focus();

        return;
    }

    form.email.value = form.email.value.trim();
    if (form.email.value.length == 0) {
        alert('이메일을 입력해주세요.');
        form.email.focus();

        return;
    }

    form.location.value = form.location.value.trim();
    if (form.location.value.length == 0) {
        alert('지역을 선택해주세요.');
        form.location.focus();

        return;
    }

    form.profile.value = form.profile.value.trim();
    if (form.profile.value.length == 0) {
        alert('자기소개를 입력해주세요.');
        form.profile.focus();

        return;
    }

    form.submit();
    MemberJoin__submitDone = true;
}
// 폼체크 함수 끝

// AJax 요청으로 회원정보 체크 시작
$("#join-submit").click(function(){
    var checked = $("#join-submit").val();
    if(!checked.contains('loginId')){
        $("#loginIdCheckMsg").html('아이디 중복을 확인해주세요.');

        return false;
    }
});
$("#join-submit").click(function(){
    var checked = $("#join-submit").val();
    if(!checked.contains('email')){
        $("#emailCheckMsg").html('이메일 중복을 확인해주세요.');

        return false;
    }
});
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
$('#loginPwCheck').blur(function(){
    var loginPw = $("#loginPw").val();
    var loginPwCheck = $("#loginPwCheck").val();
    if(loginPwCheck.length == 0){
        $("#loginPwCheckMsg").html("비밀번호 확인을 입력해주세요.");
        $("#loginPwCheckMsg").css("color", "red");
    }
    else{
        if(loginPw != loginPwCheck){
            $("#loginPwCheckMsg").html("비밀번호가 서로 일치하지 않습니다.");
            $("#loginPwCheckMsg").css("color", "red");
        }
        else{
            $("#loginPwCheckMsg").html("비밀번호가 서로 일치합니다.");
            $("#loginPwCheckMsg").css("color", "green");
        }
    }
});
// AJax 요청으로 회원정보 체크 끝
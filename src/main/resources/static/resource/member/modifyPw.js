function MemberModifyPw__submit(form) {


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


    form.loginPwInput.value = sha256(form.loginPw.value);
    form.loginPw.value = '';
    form.loginPwConfirm.value = '';



    const post$ = rxjs.ajax.ajax.post(
    						'/usr/member/doModifyPw',
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



$('#loginPw').blur(function(){
    var loginPw = $("#loginPw").val();
    var loginPwConfirm = $("#loginPwConfirm").val();
//  (1) 입력 여부 검사
    if(loginPw.length == 0){
        $("#loginPwCheckMsg").html("비밀번호를 입력해주세요.");
        $("#loginPwCheckMsg").css("color", "red");
        $("#modifyPw-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (시작)
    var regex = /^[a-zA-Z0-9]{8,16}$/;

    if(!regex.test(loginPw)){
        $("#loginPwCheckMsg").html('8~16자의 영대소문자 조합만 가능합니다.');
        $("#loginPwCheckMsg").css("color", "red");
        $("#modifyPw-submit").attr("disabled", true);
        return;
    }

//  (2) 입력값에 대한 정규표현식 적용 (끝)

        $("#loginPwCheckMsg").html("사용 가능한 비밀번호입니다.");
        $("#loginPwCheckMsg").css("color", "green");
        $("#modifyPw-submit").attr("disabled", false);
        return;


});





$('#loginPwConfirm').blur(function(){
    var loginPw = $("#loginPw").val();
    var loginPwConfirm = $("#loginPwConfirm").val();
    if(loginPwConfirm.length == 0){
        $("#loginPwConfirmCheckMsg").html("비밀번호를 한 번 더 입력해주세요.");
        $("#loginPwConfirmCheckMsg").css("color", "red");
        $("#modifyPw-submit").attr("disabled", true);
        return;
    }
    var regex1 = /^[a-zA-Z0-9]{8,16}$/;
    if(!regex1.test(loginPwConfirm)){
            $("#loginPwConfirmCheckMsg").html("8~16자의 영대소문자 조합만 가능합니다.");
            $("#loginPwConfirmCheckMsg").css("color", "red");
            $("#modifyPw-submit").attr("disabled", true);
            return;
    }
        if(loginPw != loginPwConfirm){
            $("#loginPwConfirmCheckMsg").html("비밀번호가 서로 일치하지 않습니다.");
            $("#loginPwConfirmCheckMsg").css("color", "red");
            $("#modifyPw-submit").attr("disabled", true);
            return;
        }

            $("#loginPwConfirmCheckMsg").html("비밀번호가 서로 일치합니다.");
            $("#loginPwConfirmCheckMsg").css("color", "green");
            $("#modifyPw-submit").attr("disabled", false);
            return;
});
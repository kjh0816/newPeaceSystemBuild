// 제출 클릭 시, 입력 여부 검사 및 Controller 비동기 요청
function MemberFindPw__submit(form){
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

            const post$ = rxjs.ajax.ajax.post(
                						'/usr/member/doFindPw',
                						new FormData(form)
                					);

                					post$.subscribe(
                						res => {
                							if ( res.response.success ) {
                        //                      회원가입 성공일 경우, 로그인 아이디를 파라미터로 가지고 로그인 페이지로 replace시킨다.
                                                alert(res.response.msg);
                                                window.location.replace('/usr/member/login');
                							}
                							else {
                    //                                    회원가입 실패일 경우, 실패 원인 메시지를 띠워준다.
                                                alert(res.response.msg);
                							}
                						}
                					);


}




// 이메일 양식 비동기 검사
$('#email').blur(function(){
    var email = $('#email').val();
    $("#findPw-submit").val() + 'email';
//   (1) 입력 여부 검사
    if(email.length == 0){
                    $("#emailCheckMsg").html('이메일을 입력해주세요.');
                    $("#emailCheckMsg").css("color", "red");
                    $("#findPw-submit").attr("disabled", true);
                    return;
                }
    var regex = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/;
        if(!regex.test(email)){
            $("#emailCheckMsg").html('올바른 이메일 형식이 아닙니다.');
            $("#emailCheckMsg").css("color", "red");
            $("#findPw-submit").attr("disabled", true);
            return;
        }
    //  (2) 입력값에 대한 정규표현식 적용 (끝)
            $("#emailCheckMsg").html("");
            $("#emailCheckMsg").css("color", "green");
            $("#findPw-submit").attr("disabled", false);
            return;
});

// 이름 입력 양식 검사
$('#loginId').blur(function(){
    var loginId = $("#loginId").val();
    var loginIdCheckMsg = $("#loginIdCheckMsg").val();
//  (1) 입력 여부 검사
    if(loginId.length == 0){
        $("#loginIdCheckMsg").html("아이디를 입력해주세요.");
        $("#loginIdCheckMsg").css("color", "red");
        $("#findPw-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (시작)
    var regex = /^[a-z0-9]{6,20}$/;
    if(!regex.test(loginId)){
        $("#loginIdCheckMsg").html('6~20자의 영문 소문자, 숫자만 가능합니다.');
        $("#loginIdCheckMsg").css("color", "red");
        $("#findPw-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (끝)
        $("#loginIdCheckMsg").html("");
        $("#loginIdCheckMsg").css("color", "green");
        $("#findPw-submit").attr("disabled", false);
        return;
});
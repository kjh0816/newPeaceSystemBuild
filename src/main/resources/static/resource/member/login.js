// 폼체크 함수 시작

function MemberLogin__submit(form) {




    if (form.loginId.value.length == 0) {
        swal({
            title: "아이디를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginId.focus();

        return;
    }

    if (form.loginPw.value.length == 0) {
        swal({
            title: "비밀번호를 입력해주세요.",
            icon: "info",
            button: "확인",
        });
        form.loginPw.focus();

        return;
    }

    form.loginPwInput.value = sha256(form.loginPw.value);
    form.loginPw.value = '';



    var loginId = form.loginId.value;
    var loginPwInput = form.loginPwInput.value;




    $.ajax({
                    type: 'POST',
                    url: './doLogin',
                    data: {
                    loginId:loginId
                    , loginPwInput:loginPwInput
                    },
                    dataType: 'json',
                    success: function(result){
//                      Controller에서 doLogin은 ResultData(String)을 return한다.

//                      로그인 실패일 경우, 실패 원인 메시지를 띠워준다.
                        if(result.resultCode.startsWith("F-")){
                            alert(result.msg);
                        }
//                      로그인 성공일 경우, replaceUri를 Map으로 같이 return 해주는데, 이곳으로 replace시킨다.
                        if(result.resultCode.startsWith("S-")){
                            location.replace(result.map.replaceUri);
                        }


                    }

                });


}
// 폼체크 함수 끝
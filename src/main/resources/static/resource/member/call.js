function MemberCall__submit(form) {


    form.deceasedName.value = form.deceasedName.value.trim();
    if (form.deceasedName.value.length == 0) {
            swal({
                title: "고인의 성함을 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.deceasedName.focus();

            return;
    }

    form.relatedName.value = form.relatedName.value.trim();
    if (form.relatedName.value.length == 0) {
            swal({
                title: "상주의 성함을 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.relatedName.focus();

            return;
    }

    form.cellphoneNo.value = form.cellphoneNo.value.trim();
    if (form.cellphoneNo.value.length == 0) {
            swal({
                title: "상주의 연락처를 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.cellphoneNo.focus();

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

    form.address.value = form.address.value.trim();
    if (form.address.value.length == 0) {
            swal({
                title: "상세 주소를 입력해주세요.",
                icon: "info",
                button: "확인",
            });
            form.address.focus();

            return;
    }

    const post$ = rxjs.ajax.ajax.post(
    //              고인의 정보를 올바르게 입력했을 경우, client 테이블에 row가 추가된다.
    //              clientId를 추가 정보로 return한다.
                    '/usr/member/doCall',
                    new FormData(form)
                );

    post$.subscribe(
        res => {


            if ( res.response.success ) {

//          doCall에서 return된 clientId를 URL 파라미터로 넘겨준다.
                const clientId = res.response.map.clientId;

                alert(res.response.msg);
                window.location.replace('/usr/member/progress?clientId=' + clientId);
            }
            else {
//              고인 정보가 제대로 입력되지 않은 경우
                alert(res.response.msg);
            }
        }
    );

}

$('#deceasedName').blur(function(){
    var deceasedName = $("#deceasedName").val();
//  (1) 입력 여부 검사
    if(deceasedName.length == 0){
        $("#deceasedNameCheckMsg").html("성함을 입력해주세요.");
        $("#deceasedNameCheckMsg").css("color", "red");
        $("#call-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (시작)
    var regex = /^[가-힣]{2,17}$/;
    if(!regex.test(deceasedName)){
        $("#deceasedNameCheckMsg").html('한글만 입력해주세요.');
        $("#deceasedNameCheckMsg").css("color", "red");
        $("#call-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (끝)
        $("#deceasedNameCheckMsg").html("");
        $("#deceasedNameCheckMsg").css("color", "green");
        $("#call-submit").attr("disabled", false);
        return;
});

$('#relatedName').blur(function(){
    var deceasedName = $("#relatedName").val();
//  (1) 입력 여부 검사
    if(deceasedName.length == 0){
        $("#relatedNameCheckMsg").html("성함을 입력해주세요.");
        $("#relatedNameCheckMsg").css("color", "red");
        $("#call-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (시작)
    var regex = /^[가-힣]{2,17}$/;
    if(!regex.test(deceasedName)){
        $("#relatedNameCheckMsg").html('한글만 입력해주세요.');
        $("#relatedNameCheckMsg").css("color", "red");
        $("#call-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (끝)
        $("#relatedNameCheckMsg").html("");
        $("#relatedNameCheckMsg").css("color", "green");
        $("#call-submit").attr("disabled", false);
        return;
});

$('#cellphoneNo').blur(function(){
    var cellphoneNo = $("#cellphoneNo").val();
//  (1) 입력 여부 검사
    if(cellphoneNo.length == 0){
        $("#cellphoneNoCheckMsg").html("핸드폰 번호를 입력해주세요.");
        $("#cellphoneNoCheckMsg").css("color", "red");
        $("#call-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (시작)
    var regex = /^[0-9]{9,13}$/;
    if(!regex.test(cellphoneNo)){
        $("#cellphoneNoCheckMsg").html('핸드폰 번호가 올바른지 확인해주세요.');
        $("#cellphoneNoCheckMsg").css("color", "red");
        $("#call-submit").attr("disabled", true);
        return;
    }
//  (2) 입력값에 대한 정규표현식 적용 (끝)
        $("#cellphoneNoCheckMsg").html("");
        $("#cellphoneNoCheckMsg").css("color", "green");
        $("#call-submit").attr("disabled", false);
        return;
});
// 제출 ( 시작 )
function DirectorModifyFuneral__submit(form){

const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doModifyFuneral',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if (res.response.success) {
                    // 성공 시, 입력 및 수정을 끝내고 progress 페이지로 이동한다.
                   alert(res.response.msg);
                   window.location.href="/usr/director/progress?clientId=" + res.response.map.client.id;
            }
            else {
                   alert(res.response.msg);
                   window.location.replace(res.response.map.replaceUrl);
            }
        }
    );
}
// 제출  ( 끝 )

// 오늘 날짜를 yyyy-mm-dd 형식으로 출력하는 함수
function getToday(){
    var date = new Date();
    var year = date.getFullYear();
    var month = ("0" + (1 + date.getMonth())).slice(-2);
    var day = ("0" + date.getDate()).slice(-2);

    return year + "-" + month + "-" + day;
}

// department(시/도)를 선택했을 때, Ajax 요청을 통해 조회 후 정보 반환
$('#department').change(function(){

    var department = $('#department').val().trim().toString();

     $.ajax({
                    type: 'POST',
                    url: './departmentDetail',
                    dataType: 'json',
                    data: {department:department},
                    success: function(result){


                        // 1) SELECT BOX의 기존 내용을 지운다.
                        $('#departmentDetail').empty();
                        $('#funeralHallName').empty();
                        // 2) SELECT BOX의 대표 기준값을 넣어준다. (선택 불가 option)
                        $('#departmentDetail').append("<option disabled selected>시/군/구</option>")
                        $('#funeralHallName').append("<option disabled selected>장례식장</option>")
                        // 3) SELECT BOX에 option을 추가한다.
                        for(var count = 0; count < result.map.departmentDetails.length; count++){
                            var option = $("<option>" + result.map.departmentDetails[count] + "</option>");
                            $('#departmentDetail').append(option);
                        }
                    }

                });
});


// department(시/도)를 선택했을 때, Ajax 요청을 통해 조회 후 정보 반환
$('#departmentDetail').change(function(){

    var departmentDetail = $('#departmentDetail').val().trim().toString();


    $.ajax({
        type: 'POST',
        url: './funeralHallName',
        dataType: 'json',
        data: {departmentDetail:departmentDetail},
        success: function(result){

            // 1) SELECT BOX의 기존 option들을 지운다.
            $('#funeralHallName').empty();
            // 2) SELECT BOX의 제목을 넣어준다. (선택 불가 option)
            $('#funeralHallName').append("<option disabled selected>장례식장</option>")
            // 3) SELECT BOX에 option을 추가한다.
            for(var count = 0; count < result.map.funeralHallNames.length; count++){
                var option = $("<option>" + result.map.funeralHallNames[count] + "</option>");
                $('#funeralHallName').append(option);
            }

        }
    });

});



// 팝업 창을 띠워주기 위한 함수(팝업은 html 파일로, 인터셉터에서 설정한 접근 권한에 영향을 받는다.)
function goPopup(){
	// 주소검색을 수행할 팝업 페이지를 호출합니다.
	// 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.
	var pop = window.open("/usr/director/jusoPopup","pop","width=570,height=420, scrollbars=yes, resizable=yes");

	// 모바일 웹인 경우, 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrMobileLinkUrl.do)를 호출하게 됩니다.
    //var pop = window.open("/popup/jusoPopup.jsp","pop","scrollbars=yes, resizable=yes");
}


function jusoCallBack(roadFullAddr){
		// 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
//		document.form.deceasedHomeAddress.value = roadFullAddr;
		document.getElementById("deceasedHomeAddress").value = roadFullAddr;
}


$('input[type=radio][name=funeralMethod]').on('click', function() {

    var checkValue = $('input[type=radio][name=funeralMethod]:checked').val();

    if(checkValue == '0'){
        // 매장을 클릭했을 때
        $('#hideCremationInput').css('display', 'none');
        $('#hideBuryInput').css('display', 'block');
    }else if(checkValue == '1'){
        // 화장을 클릭했을 때
        $('#hideCremationInput').css('display', 'block');
        $('#hideBuryInput').css('display', 'block');
    }
});


// 사망원인 또는 사망서류를 클릭했을 때
$('input[type=checkbox][name=papers], input[type=radio][name=cause]').on('click', function() {

    // 병사에 체크된 경우
    var disease = $('input[type=radio][id=disease]:checked').val();
    // 사고사에 체크된 경우
    var accident = $('input[type=radio][id=accident]:checked').val();

    // 사망진단서가 체크된 경우
    var deathDiagnosis = $('input[type=checkbox][id=deathDiagnosis]:checked').val();
    // 사체검안서가 체크된 경우
    var deathCertificate = $('input[type=checkbox][id=deathCertificate]:checked').val();
    // 검시필증이 체크된 경우
    var autopsied = $('input[type=checkbox][id=autopsied]:checked').val();

    // 사망 원인과 사망 서류의 관계 ( 시작 )
    // 1) 병사일 경우, 검시필증 체크 불가
    if(disease && autopsied){
        alert('병사일 경우, 검시필증이 필요없습니다.');
        $(this).prop("checked", false);
    }

    // 2) 사고사일 경우, 사망진단서 체크 불가
    if(accident && deathDiagnosis){
        alert('사고사일 경우, 사체검안서와 검시필증이 필요합니다.')
        $(this).prop("checked", false);
    }
    // 사망 원인과 사망 서류의 관계 ( 끝 )


    // 1) 사망진단서가 있으면, 사체 검안서나 검시필증이 필요없고, 사체검안서가 있으면 사망진단서가 필요없다.
    if(deathDiagnosis && deathCertificate){
        alert('사망진단서와 사체검안서 중 하나만 체크할 수 있습니다.');
        $(this).prop("checked", false);
    }

    // 사체검안서와 검시필증과의 관계 ( 시작 )
    if(autopsied && !deathCertificate){
        // 사체검안서가 체크되지 않은 상태에서 검시필증을 체크했을 때
        if($(this).attr('id') == 'autopsied'){
            alert('사체검안서를 확인해주십시오.');
            $(this).prop("checked", false);
        }
        // 사체검안서, 검시필증이 체크된 상태에서 사체검안서의 체크를 풀 때
        if($(this).attr('id') == 'deathCertificate'){

            // 아래 두 코드의 순서를 바꾸면, 체크박스가 깜빡거리는 게 보인다.
            $(this).prop("checked", true);
            alert('검시필증은 사체검안서가 필요합니다.')
        }
    }
    // 사체검안서와 검시필증과의 관계 ( 끝 )


});
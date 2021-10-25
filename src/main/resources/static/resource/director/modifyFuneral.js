// 제출 ( 시작 )
function DirectorModifyFuneral__submit(form){

    var funeralHallName = form.funeralHallName.value;
    var funeralHallRoom = form.funeralHallRoom.value;
    var deceasedName = form.deceasedName.value;
    var frontNum = form.frontNum.value;
    var backNum = form.backNum.value;
    var deceasedHomeAddress = form.deceasedHomeAddress.value;
    var familyClan = form.familyClan.value;
    var religion = form.religion.value;
    var duty = form.duty.value;
    var birth = form.birth.value;
    var deceasedDate = form.deceasedDate.value;

    var sex = $('input[name="sex"]:checked').val();

    var birthLunar = $('input[name="birthLunar"]:checked').val();
    var deceasedLunar = $('input[name="deceasedLunar"]:checked').val();
    var funeralMethod = $('input[name="funeralMethod"]:checked').val();

    var cremationLocation = form.cremationLocation.value;
    var buryLocation = form.buryLocation.value;

    var cause = $('input[name="cause"]:checked').val();
    var papers = $('input[name="papers"]:checked').val();
    var autopsyCheck = $('input[name="autopsyCheck"]:checked').val();

    var casketDate = form.casketDate.value;
    var casketTime = form.casketTime.value;
    var leavingDate = form.leavingDate.value;
    var leavingTime = form.leavingTime.value;

    // 상주 관련 정보
    var chiefName = form.chiefName.value;
    var chiefRelation = form.chiefRelation.value;
    var chiefCellphoneNo = form.chiefCellphoneNo.value;
    var chiefAddress = form.chiefAddress.value;
    var chiefAccountNum = form.accountNum.value;
    var chiefBank = form.bank.value;


    // 검시필증이 체크가 안 된 경우, 값을 직접 넣어준다.
    if(autopsyCheck == null){
        autopsyCheck = 'F';
    }



    $.ajax({
                        type: 'POST',
                        url: './doModifyFuneral',
                        dataType: 'json',
                        data: {
                        funeralHallName:funeralHallName,
                        funeralHallRoom:funeralHallRoom,
                        deceasedName:deceasedName,
                        frontNum:frontNum,
                        backNum:backNum,
                        deceasedHomeAddress:deceasedHomeAddress,
                        familyClan:familyClan,
                        religion:religion,
                        duty:duty,
                        funeralMethod:funeralMethod,
                        cremationLocation:cremationLocation,
                        buryLocation:buryLocation,
                        cause:cause,
                        papers:papers,
                        autopsyCheck:autopsyCheck,
                        casketDate:casketDate,
                        casketTime:casketTime,
                        leavingDate:leavingDate,
                        leavingTime:leavingTime,
                        chiefName:chiefName,
                        chiefRelation:chiefRelation,
                        chiefCellphoneNo:chiefCellphoneNo,
                        chiefAddress:chiefAddress,
                        clientId:clientId,
                        sex:sex,
                        birthLunar:birthLunar,
                        deceasedLunar:deceasedLunar,
                        chiefAccountNum:chiefAccountNum,
                        chiefBank:chiefBank
                        },
                        success: function(result){
                            alert(result.msg);
//                            window.location.replace('/usr/director/progress');
                        }
    });
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

// 현재 시간을 hh:mm:ss 형식으로 출력하는 함수
function getNow(){
    var today = new Date();

    var hours = ('0' + today.getHours()).slice(-2);
    var minutes = ('0' + today.getMinutes()).slice(-2);
    var seconds = ('0' + today.getSeconds()).slice(-2);

    var timeString = hours + ':' + minutes  + ':' + seconds;

    return timeString;
}

// 유가족 추가 버튼을 눌렀을 때 실행될 함수
function addFamily(yee){

    var familyRelation = $(yee).siblings(document.getElementById('familyName')).val();
    var familyName = $(yee).prev().prev().val();
    var familyCellphoneNo = $(yee).prev().val();

    // 입력 여부 검사 (시작)
    if(familyRelation == null){
        alert('관계를 선택해주십시오.');
        return
    }
    if(familyName.trim().length == 0){
        alert('이름을 입력해주십시오.')
        return
    }
    if(familyCellphoneNo.trim().length == 0){
        alert('핸드폰 번호를 입력해주십시오.');
        return
    }
    // 입력 여부 검사 (끝)

    // 직접입력일 경우, 입력란의 값을 취한다.
    if(familyRelation == '직접입력'){
        var familyRelation = $(yee).prev().prev().prev().val();
    }

    // 데이터 정합성 검사 (시작)
        // 관계 검사 (시작) : 1~10자의 한글만 입력 가능
            var relationRegex = /^[가-힣]{1,10}$/;
            if(!relationRegex.test(familyRelation)){
                alert('입력한 유가족 관계가 올바른지 확인해주십시오.');
                return
            }
        // 관계 검사 (끝)

        // 이름 검사 (시작) : 2~10자의 한글만 입력 가능
        var nameRegex = /^[가-힣]{2,10}$/;
        if(!nameRegex.test(familyName)){
            alert('유가족의 이름이 올바른지 확인해주십시오.');
            return
        }
        // 이름 검사 (끝)

        // 핸드폰번호 검사 (시작) : 10~11의 숫자만 입력 가능
        var cellphoneNoRegex = /^[0-9]{10,11}$/;
        if(!cellphoneNoRegex.test(familyCellphoneNo)){
            alert('유가족의 핸드폰번호가 올바른지 확인해주십시오.');
            return
        }
        // 핸드폰번호 검사 (끝)
    // 데이터 정합성 검사 (끝)

    // 서버로 데이터 전송 및 HTML 출력 (시작)

    $.ajax({
        type: 'POST',
        url: './addFamily',
        dataType: 'json',
        data: {
        familyRelation:familyRelation,
        familyName:familyName,
        familyCellphoneNo:familyCellphoneNo,
        clientId:clientId
        },
        success: function(result){
            if ( result.success ) {

                alert(result.msg);
                // 실제 보여줄 데이터를 HTML에 전달
                var htmlCodes = "<li class='flex'><input type='text' disabled class='input input-bordered w-1/12' value=" + familyRelation + "><input type='text' disabled class='input input-bordered w-1/12' value="+ familyName +"><input type='text' disabled class='input input-bordered w-2/12' value=" + familyCellphoneNo + "><i class='fas fa-times self-center text-4xl ml-3 cursor-pointer' onclick='removeFamily(this);'></i></li>";
                $('#familyList').append(htmlCodes);

            }
            else {
                alert(result.msg);
            }
        }

    });
    // 서버로 데이터 전송 및 HTML 출력 (끝)




}

// 유가족 삭제 버튼을 눌렀을 때 실행될 함수
function removeFamily(yee){

    var familyRelation = $(yee).siblings(document.getElementById('familyName')).val();
    var familyName = $(yee).prev().prev().val();
    var familyCellphoneNo = $(yee).prev().val();

    if( confirm("'" + familyName + "'" + ' 유가족님의 정보를 지우시겠습니까?') == false ){
        return false
    }


    $.ajax({
            type: 'POST',
            url: './removeFamily',
            dataType: 'json',
            data: {
            familyRelation:familyRelation,
            familyName:familyName,
            familyCellphoneNo:familyCellphoneNo,
            clientId:clientId
            },
            success: function(result){
                if ( result.success ) {
                    // 실제 보여줄 데이터를 HTML에 전달
                    $(yee).closest('li').remove();
                }
                else {
                    alert(result.msg);
                }
            }
    });
}


$('#familyRelation').change(function(){
    // $(this).val() 셀렉트 박스의 현재 값
    if($(this).val().trim() == "직접입력"){
        $(this).next().css("display", "block");
    }
    if($(this).val().trim() != "직접입력"){
            $(this).next().css("display", "none");
    }

});

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
function goPopup(inputNum){
	// 주소검색을 수행할 팝업 페이지를 호출합니다.
	// 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrLinkUrl.do)를 호출하게 됩니다.

	var pop = window.open("/usr/director/jusoPopup?inputNum=" + inputNum,"pop","width=570,height=420, scrollbars=yes, resizable=yes");

	// 모바일 웹인 경우, 호출된 페이지(jusopopup.jsp)에서 실제 주소검색URL(https://www.juso.go.kr/addrlink/addrMobileLinkUrl.do)를 호출하게 됩니다.
    //var pop = window.open("/popup/jusoPopup.jsp","pop","scrollbars=yes, resizable=yes");
}


function jusoCallBack(roadFullAddr, inputNum){
		// 팝업페이지에서 주소입력한 정보를 받아서, 현 페이지에 정보를 등록합니다.
//		document.form.deceasedHomeAddress.value = roadFullAddr;
        if(inputNum == '1'){
            document.getElementById("deceasedHomeAddress").value = roadFullAddr;
        }
        if(inputNum == '2'){
            document.getElementById("chiefAddress").value = roadFullAddr;
        }

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
$('input[type=radio][name=papers], input[type=checkbox][name=autopsyCheck], input[type=radio][name=cause]').on('click', function() {

    // 병사에 체크된 경우
    var disease = $('input[type=radio][id=disease]:checked').val();
    // 사고사에 체크된 경우
    var accident = $('input[type=radio][id=accident]:checked').val();

    // 사망진단서가 체크된 경우
    var deathDiagnosis = $('input[type=radio][id=deathDiagnosis]:checked').val();
    // 사체검안서가 체크된 경우
    var deathCertificate = $('input[type=radio][id=deathCertificate]:checked').val();
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
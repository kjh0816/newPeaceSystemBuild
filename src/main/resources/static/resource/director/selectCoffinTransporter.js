let SelectMourningCloth__submitDone = false;

function selectCoffinTransporter__submit(form) {
    if (form.deceasedHomeAddress.value.length == 0) {
        swal({
            title: "운구차량 출동 주소를 입력해주세요.",
            icon: "info",
            button: "확인",
        });

        return;
    }

    const post$ = rxjs.ajax.ajax.post(
        '/usr/director/doSelectCoffinTransporter',
        new FormData(form)
    );
    post$.subscribe(
        res => {
            if ( res.response.success ) {
                   if ( !confirm(res.response.msg) ) return false;
                   window.location.href="/usr/director/progress";
            }
            else {
                   alert(res.response.msg);
            }
        }
    );
}

// 팝업 창을 띠워주기 위한 함수(팝업은 html 파일로, 인터셉터에서 설정한 접근 권한에 영향을 받는다.)
function goPopup(inputNum){
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
            document.getElementById("departureAddress").value = roadFullAddr;
        }
        if(inputNum == '2'){
            document.getElementById("destinationAddress").value = roadFullAddr;
        }

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

$('#funeralHallName').change(function(){


    var name = $('#funeralHallName').val().trim().toString();
    $.ajax({
        type: 'POST',
        url: './funeralHallNum',
        dataType: 'json',
        data: {name:name},
        success: function(result){


            $('#funeralHallNum').empty();
            $('#funeralHallNum').append("<div class='badge badge-md'>장례식장 연락처</div>");
            $('#funeralHallNum').append("<p class='pl-2'>" + result.map.cellphoneNo + "</p>");
            $('#funeralHallNum').append("<a href='tel:" + result.map.cellphoneNo + "'" + "class='btn btn-active ml-10'>전화걸기</a>");

        }
    });
});

// 장례식장을 클릭했을 때
$('#funeralInput').click(function(){

});
// 직접 입력을 클릭했을 때
$('#addrInput').click(function(){

});
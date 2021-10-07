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
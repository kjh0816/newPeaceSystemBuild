

function init(){



//    document.domain = "http://localhost:8050";

	var url = window.location.href;
	var confmKey = "devU01TX0FVVEgyMDIxMDkzMDEyNTg1NDExMTcwMzg=";
	var resultType = "4"; // 도로명주소 검색결과 화면 출력내용, 1 : 도로명, 2 : 도로명+지번+상세보기(관련지번, 관할주민센터), 3 : 도로명+상세보기(상세건물명), 4 : 도로명+지번+상세보기(관련지번, 관할주민센터, 상세건물명)
	var inputYn= document.getElementById("inputYn").value;
	if(inputYn != "Y"){

		document.form.confmKey.value = confmKey;
		document.form.returnUrl.value = url;
		document.form.resultType.value = resultType;
		document.form.action="https://www.juso.go.kr/addrlink/addrLinkUrl.do"; //인터넷망
		//document.form.action="https://www.juso.go.kr/addrlink/addrMobileLinkUrl.do"; //모바일 웹인 경우, 인터넷망
		document.form.submit();
	}else{

		opener.jusoCallBack(
		document.getElementById("roadFullAddr").value
		);
		window.close();
		}
}


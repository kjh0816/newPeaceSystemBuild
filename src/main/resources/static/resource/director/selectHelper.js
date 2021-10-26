
var helperCount = 0;

function addHelper(yee){

    var department = $(yee).siblings(document.getElementById('department')).val();
    var workDate = $(yee).prev().prev().prev().val();
    var workStartTime = $(yee).prev().prev().val();
    var workFinishTime = $(yee).prev().val();

    // 입력 여부 검사 (시작)
    if(department == null){
        alert('시도를 선택해주십시오.');
        return
    }
    if(workDate.length == 0 || workDate == null){
        alert('근무날짜를 선택해주십시오.')
        return
    }
    if(workStartTime == null){
        alert('근무시작 시간을 선택해주십시오.');
        return
    }
    if(workFinishTime == null){
        alert('근무종료 시간을 선택해주십시오.');
        return
    }
    // 입력 여부 검사 (끝)
    helperCount++;

    alert(helperCount + '번째 도우미가 추가되었습니다.');


    // 실제 보여줄 데이터를 HTML에 전달
    var htmlCodes = "<tr><th>" + helperCount + "</th><td><input type='text' disabled class='input input-bordered' value=" + department + "></td><td><input type='text' disabled class='input input-bordered' value="+ workDate +"></td><td><input type='text' disabled class='input input-bordered' value=" + workStartTime + "></td><td><input type='text' disabled class='input input-bordered' value=" + workFinishTime + "></td><td><i class='fas fa-times self-center text-4xl ml-3 cursor-pointer' onclick='removeHelper(this);'></i></td></tr>";
    $('#helperList').append(htmlCodes);

    // 서버로 데이터 전송 및 HTML 출력 (끝)




}


// 제출 시 이루어져야하는 것들 (1) 도우미 개인마다 다른 문자 메시지 전송 (일괄 호출)
function DirectorSelectHelper__submit(form){

}
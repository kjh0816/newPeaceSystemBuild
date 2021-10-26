
// 해당 도우미에 대한 일정 추가 시 (1번은 이미 나와있는 최소 입력값)
var scheduleCount = 1;

// 도우미 추가 완료 시
var helperCount = 0;
function addSchedule(yee){

    scheduleCount++;

    var schedule = "<div class='flex' id='scheduleBlock'><div class='w-1/12'></div><input type='date' id='workDate" + scheduleCount + "' name='workDate"+scheduleCount+"' class='input input-bordered w-2/12'><select id='workStartTime"+scheduleCount+"' name='workStartTime"+scheduleCount+"' class='select select-bordered w-2/12'><option value='' disabled selected>근무시작 시간</option><option value='10:00'>10:00</option><option value='12:00'>12:00</option></select><select id='workFinishTime"+scheduleCount+"' name='workFinishTime"+scheduleCount+"' class='select select-bordered w-2/12'><option value='' disabled selected>근무종료 시간</option><option value='20:00'>20:00</option><option value='22:00'>22:00</option></select><button type='button' class='btn' onclick='addSchedule(this);'>근무일정 추가</button><i class='fas fa-times self-center text-4xl ml-3 cursor-pointer' onclick='removeSchedule(this);'></i></div>"

    $('#scheduleList').append(schedule);

}

function removeSchedule(yee){

    $(yee).closest('#scheduleBlock').remove();
}

function addHelper(yee){

    var department = $(yee).siblings(document.getElementById('department')).val();
    var workDate = $(yee).prev().prev().prev().prev().prev().val();
    var workStartTime = $(yee).prev().prev().prev().prev().val();
    var workFinishTime = $(yee).prev().prev().prev().val();
    var helperNum = $(yee).prev().val();


    // 입력 여부 검사 (시작) ※ 필수 입력값은 첫번째 로우로만 한다.
    if(department == null){
        alert('시도를 선택해주십시오.');
        return
    }
    if(workDate1.length == 0 || workDate == null){
        alert('근무날짜를 선택해주십시오.')
        return
    }
    if(workStartTime1 == null){
        alert('근무시작 시간을 선택해주십시오.');
        return
    }
    if(workFinishTime1 == null){
        alert('근무종료 시간을 선택해주십시오.');
        return
    }
    if(helperNum.length == 0){
        alert('인원 수를 최소 1명 이상 입력해주십시오.');
        return
    }
    if(isNaN(helperNum)){
        alert('인원 수는 숫자만 입력해주십시오.');
        return
    }
    // 입력 여부 검사 (끝)
    helperCount++;

    alert(helperCount + '번째 도우미가 추가되었습니다.');


    // 실제 보여줄 데이터를 HTML에 전달
    var htmlCodes = "<tr><th>" + helperCount + "</th><td><input type='text' disabled class='input input-bordered' value=" + department + "></td><td><input type='text' disabled class='input input-bordered' value="+ workDate +"></td><td><input type='text' disabled class='input input-bordered' value=" + workStartTime + "></td><td><input type='text' disabled class='input input-bordered' value=" + workFinishTime + "></td><td><input type='text' disabled class='input input-bordered' value=" + helperNum + "></td><td><i class='fas fa-times self-center text-4xl ml-3 cursor-pointer' onclick='removeHelper(this);'></i></td></tr>";
    $('#helperList').append(htmlCodes);

    // 서버로 데이터 전송 및 HTML 출력 (끝)
}


// 제출 시 이루어져야하는 것들 (1) 도우미 개인마다 다른 문자 메시지 전송 (일괄 호출)
function DirectorSelectHelper__submit(form){

}
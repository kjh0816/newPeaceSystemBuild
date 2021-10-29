
// 해당 도우미에 대한 일정 추가 시 (1번은 이미 나와있는 최소 입력값)
var scheduleCount = 1;

// 일괄 호출 시, 이중 반복문의 j 값이 된다.
var maxScheduleCount = 1;

// 도우미 추가 완료 시 (일괄 호출 시, 이중 반복문의 i 값이 된다.)
var helperCount = 0;

// 같은 일감으로 묶을 수 있는 번호
var packageCount = 1;
// 호출할 총 도우미 수
var totalHelperNum = 0;

function isDefined(str){
    return str !== undefined;
}

function addSchedule(yee){

    scheduleCount++;

    var schedule = "<div class='flex' id='scheduleBlock'><div class='w-1/12'></div><input type='date' id='workDate" + scheduleCount + "' name='workDate"+scheduleCount+"' class='input input-bordered w-2/12'><select id='workStartTime"+scheduleCount+"' name='workStartTime"+scheduleCount+"' class='select select-bordered w-2/12'><option value='' disabled selected>근무시작 시간</option><option value='10:00'>10:00</option><option value='12:00'>12:00</option></select><select id='workFinishTime"+scheduleCount+"' name='workFinishTime"+scheduleCount+"' class='select select-bordered w-2/12'><option value='' disabled selected>근무종료 시간</option><option value='20:00'>20:00</option><option value='22:00'>22:00</option></select><i class='fas fa-times self-center text-4xl ml-3 cursor-pointer' onclick='removeSchedule(this);'></i></div>"

    $('#scheduleList').append(schedule);

}

function removeSchedule(yee){

    $(yee).closest('#scheduleBlock').remove();
}

function addHelper(yee){

    var department = $(yee).siblings(document.getElementById('department')).val();
    var workDate1 = $(yee).prev().prev().prev().prev().prev().val();
    var workStartTime1 = $(yee).prev().prev().prev().prev().val();
    var workFinishTime1 = $(yee).prev().prev().prev().val();
    var helperNum = $(yee).prev().val();

    // 입력 여부 검사 (시작) ※ 필수 입력값은 첫번째 로우로만 한다.
    if(department == null){
        alert('시도를 선택해주십시오.');
        return
    }
    if(workDate1.length == 0 || workDate1 == null){
        alert('근무날짜를 선택해주십시오.');
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


    // 추가된 도우미 수만큼
    helperCount++;
    // helperNum은 String 타입이므로 parsing해서 더해준다.
    totalHelperNum += parseInt(helperNum);



    alert(helperNum + '명의 도우미가 추가되었습니다.');

    var actualScheduleCount = 1;



    // 실제 입력된 스케줄의 수를 파악한다(이후, rowspan의 값이 된다.) workDate, workStartTime, workFinishTime 이 모두 입력된 경우만
    for(i = 2; i <= scheduleCount; i++){
        // workDate의 길이를 따지기 위해서는 해당 값이 define 돼야하므로, define이 됐는지 먼저 따진다.
        if(isDefined($('#workDate'+i).val()) && isDefined($('#workStartTime'+i).val()) && isDefined($('#workFinishTime'+i).val())){
            // 각 id 가 define 돼도, 값이 없을 경우, 유효하지 않다.
            if($('#workDate'+i).val().length != 0 && $('#workStartTime'+i).val() != null && $('#workFinishTime'+i).val() != null){
                actualScheduleCount++;
            }

        }
        // i가 최대값에 도달했을 때, i가 maxScheduleCount 보다 크다면, maxScheduleCount에 i를 할당한다.
        if(i == scheduleCount){
            if(i > maxScheduleCount){
                maxScheduleCount = i;
            }
        }
    }


    // 출력 (시작)
    // 하나의 도우미 단락을 생성할 때, 고유 id를 부여해준다
    // (1) 각각의 데이터가 다른 경우,
    // ex) workDate1-2 ( 1 = helperCount, 2 = actualScheduleCount )
    // (2) 도우미 공통 데이터인 경우(helperCount, department, helperNum)
    // ex) department1 ( 1 = helperCount )
    var htmlCodes = "<tr><th class='hidden' rowspan='"+ actualScheduleCount +"'><input type='hidden' disabled class='input input-bordered' id='packageCount"+packageCount+"' value='" + packageCount + "'></th><td rowspan='"+ actualScheduleCount +"'><input type='text' disabled class='input input-bordered' id='department"+helperCount+"-1' value='" + department + "'></td><td><input type='text' disabled class='input input-bordered' id='workDate"+helperCount+"-1' value="+ workDate1 +"></td><td><input type='text' disabled class='input input-bordered' id='workStartTime"+helperCount+"-1' value=" + workStartTime1 + "></td><td><input type='text' disabled class='input input-bordered' id='workFinishTime"+helperCount+"-1' value=" + workFinishTime1 + "></td><td rowspan='"+ actualScheduleCount +"'><input type='text' disabled class='input input-bordered' id='helperNum"+helperCount+"-1' value=" + helperNum + "></td><td rowspan='"+ actualScheduleCount +"'><i class='fas fa-times self-center text-4xl ml-3 cursor-pointer' onclick='removeHelper(this);'></i></td></tr>";
    $('#helperList').append(htmlCodes);


    // 추가된 스케줄들을 반복문을 통해 갯수만큼 출력한다.

    for(i = 2; i <= scheduleCount; i++){
        if(isDefined($('#workDate'+i).val()) && isDefined($('#workStartTime'+i).val()) && isDefined($('#workFinishTime'+i).val())){

            if($('#workDate'+i).val().length != 0 && $('#workStartTime'+i).val() != null && $('#workFinishTime'+i).val() != null){
                var workDate = $('#workDate'+i).val();
                var workStartTime = $('#workStartTime'+i).val();
                var workFinishTime = $('#workFinishTime'+i).val();

                var htmlCodes2 = "<tr><td><input type='text' disabled class='input input-bordered' id='workDate"+helperCount+"-"+i+"' value='"+ workDate +"'></td><td><input type='text' disabled class='input input-bordered' id='workStartTime"+helperCount+"-"+i+"' value='" + workStartTime + "'></td><td><input type='text' disabled class='input input-bordered' id='workFinishTime"+helperCount+"-"+i+"' value='" + workFinishTime + "'></td></tr>";
                $('#helperList').append(htmlCodes2);
            }

        }
    }


    // HTML 출력 (끝)

    // 기존 입력된 근무일정들을 지운다.
    $('#scheduleList').empty();
    // 추가된 스케줄 전역변수를 1로 초기화한다.
    scheduleCount = 1;
    packageCount++;



}

function removeHelper(yee){

    $(yee).closest("tr").remove();
}




// 제출 시 이루어져야하는 것들 (1) 도우미 개인마다 다른 문자 메시지 전송 (일괄 호출)
function DirectorSelectHelper__submit(form){
    if(totalHelperNum == 0){
         alert('입력된 도우미 근무 정보가 없습니다.')
         return;
    }
    if ( confirm("현재 입력된 " + totalHelperNum + "명의 도우미를 호출하시겠습니까?") == false ) return false;


    var list = new Array();

    for(i = 1; i <= helperCount; i++){
        for(j = 1; j <= maxScheduleCount; j++){
            // package 존재 여부 체크
            if(isDefined($("#department"+i+'-1').val())){
                // package 내, 세부 데이터 존재 여부 체크
                if(isDefined($('#workDate'+i+'-'+j).val())){

                    var lowerList = new Array();
                    var obj = new Object();

                    var packageCount = $('#packageCount'+i).val();
                    var department = $('#department'+i+'-1').val();
                    var workDate = $('#workDate'+i+'-'+j).val();
                    var workStartTime = $('#workStartTime'+i+'-'+j).val();
                    var workFinishTime = $('#workFinishTime'+i+'-'+j).val();
                    var helperNum = $('#helperNum'+i+'-1').val();

                    obj = {
                        packageCount : packageCount,
                        department : department,
                        workDate : workDate,
                        workStartTime : workStartTime,
                        workFinishTime : workFinishTime,
                        helperNum : helperNum
                    };

                    lowerList.push(obj);
                    list.push(lowerList);
                }
            }
        }
    }
    var jsonStr = JSON.stringify(list)


    $.ajax({
        type: 'POST',
        url: './doSelectHelper',
        dataType: 'json',
        data:{
            clientId:clientId,
            jsonStr:jsonStr
        },
        success: function(result){
            if(result.success){

            }else{

            }
        }
    });

}


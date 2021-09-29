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
                        // 2) SELECT BOX의 대표 기준값을 넣어준다. (선택 불가 option)
                        $('#departmentDetail').append("<option disabled selected>시/군/구</option>")
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
            for(var count = 0; count < result.map.funeralHallNames.size; count++){
                var option = $("<option>" + result.map.funeralHallNames[count] + "</option>");
                $('#funeralHallName').append(option);
            }

        }
    });

});
// department(시/도)를 선택했을 때, Ajax 요청을 통해 조회 후 정보 반환
$('#department').blur(function(){


    var department = $('#department').val().trim().toString();

    alert(department);





     $.ajax({
                            type: 'POST',
                            url: './departmentDetail',
                            dataType: 'json',
                            data: {department:department},
                            success: function(result){


                                // 1) SELECT BOX의 기존 내용을 지운다.
                                // $('#departmentDetail').empty();
                                // 2) SELECT BOX에 option을 추가한다.

                                for(var count = 0; count < result.map.departmentDetails.length; count++){
                                    var option = $("<option>" + result.map.departmentDetails[count] + "</option>");
                                    $('#departmentDetail').append(option);
                                }
                            }


                        });
});
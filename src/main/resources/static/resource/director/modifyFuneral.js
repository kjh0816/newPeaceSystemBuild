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

                                alert('그가 깨어났습니다.');
                            }


                        });
});
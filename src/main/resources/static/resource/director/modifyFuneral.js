// department(시/도)를 선택했을 때, Ajax 요청을 통해 조회 후 정보 반환
$('#department').blur(function(){


    var department = $('#department').val();





//    $.ajax({
//                            type: 'POST',
//                            url: './',
//                            dataType: 'json',
//                            data: {department:department},
//                            success: function(result){
//
//
//                            //   (2) ResultData에 따른 결과값 표시
//                                if(result.resultCode.startsWith("F-")){
//                                    $("#loginIdCheckMsg").html(result.msg);
//                                    $("#loginIdCheckMsg").css("color", "red");
//                                    $("#join-submit").attr("disabled", true);
//                                    return;
//                                }
//                                if(result.resultCode.startsWith("S-")){
//                                    $("#loginIdCheckMsg").html(result.msg);
//                                    $("#loginIdCheckMsg").css("color", "green");
//                                    $("#join-submit").attr("disabled", false);
//                                    return;
//                                }
//                            }
//
//                        });
});
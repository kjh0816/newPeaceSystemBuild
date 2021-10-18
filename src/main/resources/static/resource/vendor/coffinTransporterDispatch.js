
var once = true;

function coffinTransporterDispatch__submit(form){

    if(once){

        once = false;

        // 파라미터 값을 받았을 때, 배열 형태이기 때문에 형변환해준다.
        clientId = clientId.toString();

        $.ajax({
            type: 'POST',
            url: './doCoffinTransporterDispatch',
            dataType: 'json',
            data: {
            clientId:clientId
            },
            success: function(result){
                if ( result.success ) {
                    alert(result.msg);
                    window.location.replace(result.map.replaceUri);
                }
                else if ( result.fail ){
                    alert(result.msg);
                    window.location.replace(result.map.replaceUri);
                }
            }
        });
    }
}
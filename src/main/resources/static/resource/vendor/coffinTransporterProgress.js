function coffinTransporterProgress__submit(form){

     $.ajax({
                    type: 'POST',
                    dataType: 'json',
                    url: './doCoffinTransporterProgress',
                    data: {
                    clientId:clientId
                    },
                    success: function(result){
                        if(result.success){
                            alert(result.msg);
                            window.location.replace(result.map.replaceUri);
                        }else{
                            alert(result.msg);
                            window.location.replace(result.map.replaceUri);
                        }

                    }

            });
}
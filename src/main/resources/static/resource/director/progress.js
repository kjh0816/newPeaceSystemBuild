flowers.forEach (function (flower) {
    if(flowerId == flower.id){
        $('#flowers').val(flower.id).prop(selected, true);
    }
});




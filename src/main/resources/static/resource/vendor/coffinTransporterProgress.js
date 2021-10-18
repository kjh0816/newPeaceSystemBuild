function cellphoneNoFormat(){
    var cellphoneNo = $('#cellphoneNo').val();

    cellphoneNo = cellphoneNo.substr(0,3) + '-' + cellphoneNo.substr(3,7) + '-' + cellphoneNo.substr(7,cellphoneNo.length);
    $('#cellphoneNo').val('sex');
}
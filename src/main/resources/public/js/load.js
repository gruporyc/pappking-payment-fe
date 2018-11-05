$('#personType').on('change', function() {
    if(this.value == 'LEGAL'){
        $('#companyNameDiv').show();
        $('#companyName').attr('required');
        $('#naturalNameDiv').hide();
        $('#naturalLastNameDiv').hide();
        $('#firstName').removeAttr('required');
        $('#lastName').removeAttr('required');
    }else{
        $('#companyNameDiv').hide();
        $('#companyName').removeAttr('required');
        $('#naturalNameDiv').show();
        $('#naturalLastNameDiv').show();
        $('#firstName').attr('required');
        $('#lastName').attr('required');
    }
});

$('input[name=paymentMethod]').click(function() {
    $('.paymentForm').hide();
    $('#' + this.value.toLowerCase() + 'Form').show();
    switch(this.value) {
        case "CREDIT":
            $('.cc-control').attr('required');
            $('.transfer-control').removeAttr('required');
            break;
        case "PSE":
            $('.cc-control').removeAttr('required');
            $('.transfer-control').attr('required');
            break;
        default:
            $('.cc-control').removeAttr('required');
            $('.transfer-control').removeAttr('required');
    }
});
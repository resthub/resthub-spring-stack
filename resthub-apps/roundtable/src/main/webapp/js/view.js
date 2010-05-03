$.widget("roundtable.viewPoll", {
    options: {
        data : {}
    },
    _init: function() {
        this.element.render('template/poll/view.html', this.options.data);
    
        $('#vote td.no').click(function() {
            $(this).toggleClass('no');
            $(this).toggleClass('yes');
        });
        $('#vote-button').click(function() {
            var voter = $('#you input:text').val();
            if (voter != '') {
                var votes = "";
                $('#vote td.no').each(function() {
                    votes += $(this).attr('id') .split('-')[1] + ':no';
                });
                $('#vote td.yes').each(function() {
                    votes += $(this).attr('id') .split('-')[1] + ':yes';
                });
                alert('let\'s vote : ' + votes);
            }
            else {
                alert("Please fill your name");
            }
        });

    }
});
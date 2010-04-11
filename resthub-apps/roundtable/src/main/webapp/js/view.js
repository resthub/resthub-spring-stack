/**
 * Round Table visualization component.
 */
var RoundTableViewComponent = Class.extend({
    // constructor
    init: function(anchor, poll) {
        this.anchor = anchor;
        this.anchor.ejs('template/poll/view.ejs', poll);
        
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
    },
    // methods
    toString: function() {
        return "";
    }
});

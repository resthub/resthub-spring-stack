/**
 * Round Table visualization component.
 */
var RoundTableViewComponent = new JS.Class({
    // constructor
    initialize: function(anchor, poll) {
        this._anchor = anchor;
        this.template = new EJS({url: 'template/poll/view.ejs'});

        this.template.update(this._anchor, poll);

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
}, {
    // properties
    getset: [['Anchor', '_anchor']]
});

/**
 * Round Table List component.
 */
var RoundTableListComponent = Class.extend({
    // constructor
    init: function(anchor, context, polls) {
        this.anchor = anchor;
        this.anchor.ejs('template/poll/list.ejs', polls);

        $('li.poll-item').click(function() {
            var id = $(this).attr('id').split("-")[1];
            context.redirect('#/poll', id);
        });
    },
    // methods
    toString: function() {
        return "";
    }
});
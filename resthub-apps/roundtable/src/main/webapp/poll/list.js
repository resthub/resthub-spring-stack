$.widget("roundtable.listPoll", {
    options: {
        data : {},
        context : null
    },
    _init: function() {
        this.element.render('poll/list.html', this.options.data);

        var context = this.options.context;
        $('li.poll-item').click(function() {
            var id = $(this).attr('id').split("-")[1];
            context.redirect('#/poll', id);
        });
    },
    destroy: function() {
       alert("boom");
       $.Widget.prototype.destroy.apply(this, arguments); // default destroy
   }
});
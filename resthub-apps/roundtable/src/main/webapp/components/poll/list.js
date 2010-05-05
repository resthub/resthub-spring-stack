$.widget("roundtable.listPoll", {
    options: {
        data : {},
        template : 'components/poll/list.html',
        context : null
    },
    _create: function() {
        this.element.addClass('rt-poll-list');
    },
    _init: function() {
        var self = this;
        this.element.render(this.options.template, this.options.data);

        this.element.find('li.poll-item').click(function() {
            var id = $(this).attr('id').split("-")[1];
            self.options.context.redirect('#/poll', id);
        });
    },
    destroy: function() {
        this.element.removeClass('rt-poll-list');
        $.Widget.prototype.destroy.call( this );
    }
});
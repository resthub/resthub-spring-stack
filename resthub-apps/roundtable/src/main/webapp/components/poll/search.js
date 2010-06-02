$.widget('roundtable.searchPoll', {
    options: {
        template : 'components/poll/search.html'
    },
    _create: function() {
        this.element.addClass('rt-poll-search');
    },
    _init: function() {
        var self = this;
		
        this.element.render(this.options.template, null);
    },
    destroy: function() {
        this.element.removeClass('rt-poll-search');
        $.Widget.prototype.destroy.call( this );
    }
});
$.widget('roundtable.viewPoll', {
    options: {
        data : {},
        template : 'components/poll/view.html'
    },
    _create: function() {
        this.element.addClass('rt-poll-detail');
    },
    _init: function() {
        var self = this;
        this.element.render(this.options.template, this.options.data);
    
        this.element.find('tr.rt-vote-inputs td.no').click(function() {
            $(this).toggleClass('no');
            $(this).toggleClass('yes');
        });

        this.element.find('tr.rt-vote-inputs input:button').click(function() {
            var voter = self.element.find('td.rt-voter input:text').val();
            if (voter != '') {
                var votes = '';
                self.element.find('tr.rt-vote-inputs td.no').each(function() {
                    votes += $(this).attr('id') .split('-')[3] + ':no';
                });
                self.element.find('tr.rt-vote-inputs td.yes').each(function() {
                    votes += $(this).attr('id') .split('-')[3] + ':yes';
                });
                alert('let\'s vote : ' + votes);
            }
            else {
                alert('Please fill your name');
            }
        });

    },
    destroy: function() {
        this.element.removeClass('rt-poll-detail');
        $.Widget.prototype.destroy.call( this );
    }
});
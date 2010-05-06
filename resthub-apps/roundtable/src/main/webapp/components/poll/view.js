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
                var vote = {
                    voter: voter,
                    pid: self.options.data.id,
                    values: []
                }

                self.element.find('tr.rt-vote-inputs td.rt-vote').each(function() {
                    if ($(this).hasClass('yes')) {
                        vote.values.push('yes');
                    }
                    else {
                        vote.values.push('no');
                    }
                });
                alert('let\'s vote : ' + $.toJSON(vote));
                $.ajax({
                    type: 'POST',
                    url: 'api/vote',
                    dataType: 'json',
                    data: vote,
                    success: function(pid) {
                        // FIXME try to avoid this...
                        $.ajax({
                            url: 'api/poll/' + pid,
                            dataType: 'json',
                            success: function(poll) {
                               self.options.data = poll;
                               self._init();
                            }
                        });
                    }
                });
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
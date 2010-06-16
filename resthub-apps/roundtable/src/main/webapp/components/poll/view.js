(function($) {
    var viewPoll = {
        _template : 'components/poll/view.html',
        options: {
            context : null,
            url: 'api/poll/'
        },
        _create: function() {
            this.element.addClass('rt-poll-detail');
        },
        destroy: function() {
            this.element.removeClass('rt-poll-detail');
            $.Widget.prototype.destroy.call(this);
        },
        _context: function() {
            return this.options.context;
        },
        _url: function() {  
            return this.options.url;
        },
        _updatePoll: function(poll) {
            var self = this;
            this.element.render(this._template, poll);

            this.element.find('tr.rt-vote-inputs td.no').click(function() {
                $(this).toggleClass('no');
                $(this).toggleClass('yes');
            });

            this.element.find('tr.rt-vote-inputs input:button').click(function() {
                var voter = self.element.find('td.rt-voter input:text').val();
                if (voter != '') {
                    var vote = {
                        voter: voter,
                        pid: poll.id,
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

                    $.ajax({
                        type: 'POST',
                        url: 'api/vote',
                        dataType: 'json',
                        data: vote,
                        success: function(pid) {
                            $.pnotify('Voted with success.');
                            self.view(pid);
                        }
                    });
                }
                else {
                    alert('Please fill your name');
                }
            });

            this._trigger("change");
        },
        view: function(id) {
            this._context().title('Round Table - Poll #' + id);
            this._get(this._url() + id, this, '_updatePoll');
            return this;
        }
    };

    $.widget("roundtable.viewPoll", $.resthub.resthubController, viewPoll);
})(jQuery);
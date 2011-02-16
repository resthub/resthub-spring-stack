define([ 'lib/controller',
         'repositories/poll.repository'], function(Controller, PollRepository) {

    return Controller.extend("ViewPollController", {
        template : 'poll/view.html',
        poll: {},

        init : function() {
            PollRepository.read($.proxy(this, '_displayPoll'), this.poll.id);
        },
        _displayPoll : function(poll) {
            var self = this;
            this.render(poll, {
                sumVotes: function(voteIndex) {
                    var sum = 0;
                    $.each(poll.voters, function(idx, voter) {
                        sum += (voter.votes[voteIndex].value == "yes") ? 1 : 0;
                    });
                    return sum;
                }
            });

            this.element.find('tr.rt-vote-inputs td.no').click(function() {
                $(this).toggleClass('no');
                $(this).toggleClass('yes');
            });

            this.element.find('tr.rt-vote-inputs input:button').click(function() {
                var voter = self.element.find('td.rt-voter input:text').val();
                if (voter != '') {
                    var values = [];
                    self.element.find('tr.rt-vote-inputs td.rt-vote').each(function() {
                        values.push(($(this).hasClass('yes')) ? 'yes' : 'no');
                    });

                    PollRepository.vote(function(data) {
                        $.route('#/poll/' + data);
                        $.publish('voted');
                    }, voter, poll.id, values);
                }
                else {
                    alert('Please fill your name');
                }
            });
        }
    });
});


/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {
        this.get('#/', function() {
            $('#main').html('<p>Welcome to <strong>Roundtable</strong></p>');
            $('#main').listPoll("destroy");
        });

        /**
         * View polls.
         */
        this.get('#/poll/list', function(context) {
            dominoes("components/poll/list.js", function() {
                $.ajax({
                    url: 'api/poll/',
                    //url: 'test/data/polls.json',
                    dataType: 'json',
                    success: function(polls) {
                        $('#main').listPoll({
                            data : polls,
                            context: context
                        });
                    }
                });
            });
        });

       	
        /**
         * View new poll creation form.
         */
        this.get('#/poll/create', function(context) {
            dominoes("components/poll/edit.js", function() {
                $('#main').editPoll();
            });
        });

        /**
         * View poll.
         */
        this.get('#/poll/:id', function(context) {
            var id = this.params['id'];
            dominoes("components/poll/view.js", function() {
                $.ajax({
                    url: 'api/poll/' + id,
                    //url: 'test/data/poll.json',
                    dataType: 'json',
                    success: function(poll) {
                        $('#main').viewPoll({
                            data : poll
                        });
                    }
                });
            });
        });

        /**
         * Post a new poll.
         */
        this.post('#/post/poll', function(context) {
            var poll = {
                author: this.params['author'],
                topic: this.params['topic'],
                body: this.params['body'],
                answers: []
            }
            // FIXME found another way...
            var answers = this.target.answers;
            for (i = 0; i < answers.length; i++) {
                poll.answers.push({
                    body:answers[i].value
                });
            }

            $.ajax({
                type: 'POST',
                url: 'api/poll',
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                processData: false,
                data: $.toJSON(poll),
                success: function(poll) {
                    context.redirect('#/poll', poll.id);
                }
            });
        });
    });

    $(function() {
        app.run('#/');
    });
})(jQuery);

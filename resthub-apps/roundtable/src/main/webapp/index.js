
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
        this.get('#/console/wadl', function(context) {
            dominoes("lib/ui/jquery.ui.button.js lib/ui/jquery.ui.dialog.js console/wadl-console.js", function() {
				$("#main").wadlConsole();		
			});
        });


        /**
         * View polls.
         */
        this.get('#/test/poll/list', function(context) {
            dominoes("poll/list.js", function() {
                $.ajax({
                    url: 'test/poll/list.json',
                    dataType: 'json',
                    success: function(polls) {
                        $('#main').listPoll({data : polls, context: context});
                    }
                });
            });
        });

        /**
         * View polls.
         */
        this.get('#/poll/list', function(context) {
            dominoes("poll/list.js", function() {
                $.ajax({
                    url: 'api/poll',
                    dataType: 'json',
                    success: function(polls) {
                        $('#main').listPoll({data : polls, context: context});
                    }
                });
            });
        });

        /**
         * View the test poll.
         */
        this.get('#/test/poll/view', function() {
            dominoes("poll/view.js", function() {
                $.ajax({
                    url: 'test/poll/view.json',
                    dataType: 'json',
                    success: function(poll) {
                        $('#main').viewPoll({data : poll});
                    }
                });
            });
        });
		
		/**
         * View new poll creation form.
         */
        this.get('#/poll/create', function(context) {
			dominoes("poll/create.js", function() {
                $('#main').createPoll();
            });
        });

        /**
         * View poll.
         */
        this.get('#/poll/:id', function() {
            var id = this.params['id'];
            dominoes("poll/view.js", function() {
                $.ajax({
                    url: 'api/poll/' + id,
                    dataType: 'json',
                    success: function(poll) {
                        $('#main').viewPoll({data : poll});
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
        app.run();
    });
})(jQuery);

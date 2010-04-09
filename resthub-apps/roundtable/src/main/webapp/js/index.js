
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {
        this.get('#/', function() {
            $('#main').text('');
        });

        /**
         * View the test poll.
         */
        this.get('#/poll/test', function() {
        	$.ajax({
                url: 'data/poll/test.json',
                dataType: 'json',
                success: function(poll) {
        			new RoundTableViewComponent('main', poll);
                }
        	});
        });

        /**
         * View poll.
         */
        this.get('#/poll/:id', function() {
        	$.ajax({
                url: 'webresources/poll/' + this.params['id'],
                dataType: 'json',
                success: function(poll) {
                    new RoundTableViewComponent('main', poll);
                }
        	});
        });

        /**
         * View new poll creation form.
         */
        this.get('#/create', function() {
        	new RoundTableCreateComponent('main');
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
            var answers = this.params.$form[0].answers;
            for (i = 0; i < answers.length; i++) {
                poll.answers.push({body:answers[i].value});
            }

            $.ajax({
                type: 'POST',
                url: 'webresources/poll',
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

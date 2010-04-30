
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {
        this.get('#/', function() {
            $('#main').text('');
        });

        /**
         * View polls.
         */
        this.get('#/list/test', function(context) {
            dominoes("js/list.js", function() {
                $.ajax({
                    //url: 'webresources/poll',
                    url: 'data/test/polls.json',
                    dataType: 'json',
                    success: function(polls) {
                        new RoundTableListComponent($('#main'), context, polls);
                    }
                });
            });
        });

        /**
         * View polls.
         */
        this.get('#/list', function(context) {
            dominoes("js/list.js", function() {
                $.ajax({
                    url: 'webresources/poll',
                    dataType: 'json',
                    success: function(polls) {
                        new RoundTableListComponent($('#main'), context, polls);
                    }
                });
            });
        });

        /**
         * View the test poll.
         */
        this.get('#/poll/test', function() {
            dominoes("js/view.js", function() {
                $.ajax({
                    url: 'data/test/poll.json',
                    dataType: 'json',
                    success: function(poll) {
                        new RoundTableViewComponent($('#main'), poll);
                    }
                });
            });
        });

        /**
         * View poll.
         */
        this.get('#/poll/:id', function() {
            var id = this.params['id'];
            dominoes("js/view.js", function() {
                $.ajax({
                    url: 'webresources/poll/' + id,
                    dataType: 'json',
                    success: function(poll) {
                        new RoundTableViewComponent($('#main'), poll);
                    }
                });
            });
        });

        /**
         * View new poll creation form.
         */
        this.get('#/create', function() {
            dominoes("js/create.js", function() {
                new RoundTableCreateComponent($('#main'));
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
            var answers = this.params.$form[0].answers;
            for (i = 0; i < answers.length; i++) {
                poll.answers.push({
                    body:answers[i].value
                    });
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

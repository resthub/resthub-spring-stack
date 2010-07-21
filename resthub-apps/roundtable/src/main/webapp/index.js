
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {
        this.use(Sammy.Title);

        this.get('#/', function() {
            this.title('Round Table');
            $('#main').html('<p>Welcome to <strong>Roundtable</strong></p>');
            /*dominoes("components/poll/list.js", function() {
                $('#main').listPoll("destroy");
            });*/
        });

        /**
         * View polls.
         */
        this.get('#/poll', function(context) {
            var page = 0;
            if (this.params['page'] != undefined) {
                page = this.params['page']
            }

            var query = null;
            if (this.params['q'] != undefined) {
                query = this.params['q']
            }

            $('#main').listPolls({context: context});
            $('#main').listPolls("search", query, page);
        }, "components/poll/list.js");
       	
        /**
         * View new poll creation form.
         */
        this.get('#/poll/new', function(context) {
            $('#main').editPoll({context: context});
        }, 'components/poll/edit.js');

        /**
         * View poll.
         */
        this.get('#/poll/:id', function(context) {
            var id = this.params['id'];
            $('#main').viewPoll({context: context});
            $('#main').viewPoll("view", id);
        }, 'components/poll/view.js');

        /**
         * Post a new poll.
         */
        this.post('#/post/poll', function(context) {
            var poll = {
                author: this.params['author'],
                topic: this.params['topic'],
                body: this.params['body'],
                // expirationDate: (new Date(this.params['expirationDate'])).format('Y-m-d'),
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
                    $.pnotify('Poll created with success.');
                    context.redirect('#/poll', poll.id);
                }
            });
        });
    });

    $(function() {
        app.run('#/');
        $('body').ajaxError(function(e, xhr, settings, exception) {
            if (xhr.status == 404) {
                $.pnotify({
                    pnotify_title: 'Resource not found.',
                    pnotify_text: 'No objects found.'
                });
            }
            else {
                $.pnotify({
                    pnotify_title: 'Server trouble!',
                    pnotify_text: 'Error requesting ' + settings.url,
                    pnotify_type: 'error',
                    pnotify_hide: false
                });
            }
        });
        dominoes("components/poll/search.js", function() {
            $('#search').searchPoll();
        });
    });
})(jQuery);

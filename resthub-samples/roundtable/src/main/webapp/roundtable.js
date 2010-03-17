
(function($) {
    var app = $.sammy(function() {
        this.get('#/', function() {
            $('#main').text('');
        });

        /**
         * View test poll.
         */
        this.get('#/test', function() {
            $.ajax({
                url: 'data/poll/test.json',
                dataType: 'json',
                success: function(poll) {
                    new EJS({url: 'template/poll/view.ejs'}).update('main', poll);
                }
            });
        });

        /**
         * Create poll.
         */
        this.get('#/create', function() {
            $.ajax({
                url: 'data/poll/new.json',
                dataType: 'json',
                success: function(poll) {
                    new EJS({url: 'template/poll/create.ejs'}).update('main', poll);
                }
            });
        });
    });

    $(function() {
        app.run();
    });
})(jQuery);
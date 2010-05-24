
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {

        /**
         * Each "state" is declared this way
         */
        this.get('#/', function() {
            $('#main').html('<p>Welcome to home</p>');
        });

        this.get('#/test', function() {
            $('#main').html('<p>This is a test !</p>');
        });

    });

    $(function() {
        app.run('#/');
    });
    
})(jQuery);

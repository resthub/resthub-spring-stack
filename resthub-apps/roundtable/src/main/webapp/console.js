
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {
        this.get('#/', function() {
            $('#main').html('<p>Welcome to <strong>Roundtable</strong> console page.</p>');
            $('#main').listPoll("destroy");
        });
		
        /**
         * WADL console.
         */
        this.get('#/console/wadl', function(context) {
            dominoes("lib/ui/jquery.ui.button.js lib/ui/jquery.ui.dialog.js components/wadl/wadl-console.js", function() {
                $("#main").wadlConsole();
            });
        });

        /**
         * Monitoring console.
         */
        this.get('#/console/monitoring', function(context) {
            $('#main').html("<iframe src='console/monitoring'/>");
        });
        
    });

    $(function() {
        app.run();
    });
})(jQuery);


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
            dominoes("lib/ui/jquery.ui.button.js lib/ui/jquery.ui.dialog.js components/wadl/wadl-console.js $css(components/wadl/wadl-console.css) ", function() {
                $("#main").wadlConsole();
            });
        });

        /**
         * Monitoring console.
         */
        this.get('#/console/monitoring', function(context) {
			$.ajax({
                    url: 'console/monitoring',
                    dataType: 'html',
                    success: function(data) {
                        $('#main').html(data);
                    }
                });
            
        });
        
    });

    $(function() {
        app.run();
    });
})(jQuery);

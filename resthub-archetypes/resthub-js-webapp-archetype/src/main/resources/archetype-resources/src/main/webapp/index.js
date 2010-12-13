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

        this.get('#/sample/:id', function(context) {
            $('#main').viewSample({context:context, sampleId:this.params.id});
        }, 'components/sample/view.js');
    });

    $(function() {
        app.run('#/');
    });
    
})(jQuery);

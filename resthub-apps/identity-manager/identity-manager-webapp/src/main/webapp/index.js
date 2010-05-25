
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {

        this.use(Sammy.Title);
        
        this.get('#/', function() {
            this.title('Welcome to Identity Manager');
            $('#content').render('components/home.html', {})
        });

		/**
         * View users.
         */
        this.get('#/user/list', function(context) {
            $.ajax({
                url: 'api/user/',
                dataType: 'json',
                success: function(data) {
                    $('#content').listUsers({
                        data: data,
                        context: context
                    });
                }
            });
        });

		/**
         * View user.
         */
        this.get('#/user/:id', function(context) {
            var id = this.params['id'];
			$.ajax({
                url: 'api/user/'+ id,
                dataType: 'json',
                success: function(data) {
                    $('#content').viewUser({
                        data: data,
                        context: context
                    });
                }
            });
        });
    });

    $(function() {
        app.run('#/');
    });
})(jQuery);

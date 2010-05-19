
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
    });

    $(function() {
        app.run('#/');
    });
})(jQuery);

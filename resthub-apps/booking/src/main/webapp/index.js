
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {

        this.use(Sammy.Title);
        
        this.get('#/', function() {
            $('#header').render('components/header.html', {user: {}});
            $('#content').render('components/login.html', {});
            this.title('Login');
        });

        this.get('#/register', function() {
            $('#content').render('components/register.html', {});
            this.title('Register');
        });

    });

    $(function() {
        app.run('#/');
    });
})(jQuery);

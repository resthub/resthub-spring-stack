
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {

        this.use(Sammy.Title);
        
        this.get('#/', function() {
            this.title('Login');
            dominoes("components/login.js components/header.js", function() {
                $('#header').header();
                $('#content').login();
            });
        });

        this.get('#/register', function() {
            this.title('Register');
            $('#content').render('components/register.html', {});
        });

    });

    $(function() {
        app.run('#/');
    });
})(jQuery);

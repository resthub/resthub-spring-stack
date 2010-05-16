
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {

        this.use(Sammy.Title);
        
        this.get('#/', function() {
            this.title('Login');
            $('#header').render('components/header.html', {user: {}});
            $('#content').render('components/login.html', {});
        });

        this.get('#/register', function() {
            this.title('Register');
            $('#content').render('components/register.html', {});
        });

        this.post('#/user/check', function() {
            var user = {
                username: this.params['username'],
                password: this.params['password'],
                name: '',
            }
            $.ajax({
                url: 'api/user/check/',
                type: 'POST',
                contentType: 'application/json; charset=utf-8',
                dataType: 'json',
                data: $.toJSON(user),
                processData: false,
                success: function() {
                   alert("login OK !");
                },
                error: function() {
                   alert("login NOK !");
                },
                complete: function() {
                   event.preventDefault();
                   event.stopPropagation();
                }

            });
        });

    });

    $(function() {
        app.run('#/');
    });
})(jQuery);

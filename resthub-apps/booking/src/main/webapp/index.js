
/**
 * Routes
 */
(function($) {
	
    var app = $.sammy(function() {

        this.use(Sammy.Title);
        
        this.get('#/', function() {
            this.title('Login');
            $('#header').render('components/header.html', {user: $.session.getJSONItem('user')});
            $('#content').render('components/login.html', {})
        });

        this.get('#/logout', function(context) {
            $.session.clear();
            context.redirect('#/');
        });

        this.get('#/register', function() {
            this.title('Register');
            $('#content').render('components/register.html', {});
        });
        
        this.get('#/booking/list', function() {
            dominoes("components/booking/list.js", function() {
                $('#content').listBookings();
            });
        });

        this.post('#/user/check', function(context) {
            $.session.clear();
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
                success: function(user) {
                   $.session.setJSONItem('user', user);
                   // TODO replace with a less intrusive strategy based on listening events
                   $('#header').render('components/header.html', {user: $.session.getJSONItem('user')});
                   context.redirect('#/booking/list');
                },
                error: function() {
                   $("#formLogin p.messages").html('<span class="error">Bad credentials</span>');
                }

            });
        });

    });

    $(function() {
        $.session.setJSONItem('user', {});
        
        app.run('#/');
    });
})(jQuery);

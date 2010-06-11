(function($)
{
	var login =
	{
		_create: function() {

			this.options.context.store('session').clearAll();

			var user = {
				username: this.options.context.params['username'],
				password: this.options.context.params['password'],
				name: ''
			}
			this.options.user = user;
		},

		_init: function() {
			
			context = this.options.context;
			self = this;
			$.ajax({
				url: 'api/user/check/',
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				dataType: 'json',
				data: $.toJSON(this.options.user),
				processData: false,
				success: function(user) {

					context.session('user', user);

					// TODO replace with a less intrusive strategy based on listening events
					self.element.render('components/header.html', {user: user});

					context.redirect('#/home');
				},
				error: function() {
					$("#formLogin p.messages").html('<span class="error">Bad credentials</span>');
				}
			});
		},

		logout: function() {
			console.log('Logout...');
			/*Sammy.log('Logout...');
			this.store('session').clearAll();
			this.options.context.redirect('#/');*/
		},

		options: {
			user: null,
			context: null
		}
	};

	$.widget("resthub.login", login);
})(jQuery);
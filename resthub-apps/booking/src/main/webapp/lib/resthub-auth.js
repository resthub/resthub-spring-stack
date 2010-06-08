/**
 * TODO : this is dirty code...
 */

(function($)
{
	var resthubAuth =
	{
		_init: function() {
			
			this.options.context.store('session').clearAll();
			
			var user = {
				username: this.options.context.params['username'],
				password: this.options.context.params['password'],
				name: ''
			}

			context = this.options.context;
			$.ajax({
				url: 'api/user/check/',
				type: 'POST',
				contentType: 'application/json; charset=utf-8',
				dataType: 'json',
				data: $.toJSON(user),
				processData: false,
				success: function(user) {

					context.session('user', user);

					// TODO replace with a less intrusive strategy based on listening events
					$('#header').render('components/header.html', {user: user});

					context.redirect('#/home');
				},
				error: function() {
					$("#formLogin p.messages").html('<span class="error">Bad credentials</span>');
				}
			});
		},

		options: {
			context: null
		}
	};

	$.widget("resthub.resthubAuth", resthubAuth);
})(jQuery);
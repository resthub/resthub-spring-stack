(function($)
{
	var userRegister =
	{
		_init: function() {

			this.options.context.title('Register');
			this.element.render(this.options.template);

			var self = this;
			$('#register-button').bind('click', function() {
				var user = {
					email: $('input[name=email]').val(),
					username: $('input[name=username]').val(),
					password: $('input[name=password]').val(),
					fullname: $('input[name=name]').val()
				}
				self._post('api/user', self._userRegistered, $.toJSON(user));
			});
		},
		_userRegistered: function(user) {
			this.options.context.trigger('user-registered', user);
		},
		options: {
			context: null,
			template: 'components/user/register.html'
		}
	};

	$.widget("booking.userRegister", $.resthub.resthubController, userRegister);
})(jQuery);
define(['jquery.controller','jquery.json'], function() {
(function($)
{
	$.widget("booking.userRegister", $.ui.controller, {
		_init: function() {

			this.options.cx.title('Register');
			this._render();

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
			this.options.cx.trigger('user-registered', user);
		},
		options: {
			cx: null,
			template: 'user/register.html'
		}
	});
})(jQuery);
});
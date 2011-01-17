define(['jquery.controller', 'jquery.json'], function() {

	$.widget("booking.userLogin", $.ui.controller, {
		_init: function() {
			this.options.context.store('session').clearAll();
			var user = {
				username: this.options.context.params['username'],
				password: this.options.context.params['password']
			}
			this._post('api/user/check/', this._userLoggedIn, $.toJSON(user));
		},
		_userLoggedIn: function(user) {
			this.options.context.session('user', user);
			this.options.context.redirect('#/home');
			this.options.context.trigger('user-logged-in');
		},
		options: {
			context: null
		}
	});
	
});
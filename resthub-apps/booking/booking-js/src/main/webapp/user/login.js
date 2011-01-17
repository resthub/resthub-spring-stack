define(['jquery.controller', 'jquery.json'], function() {

	$.widget("booking.userLogin", $.ui.controller, {
		_init: function() {
			this.options.cx.store('session').clearAll();
			var user = {
				username: this.options.cx.params['username'],
				password: this.options.cx.params['password']
			}
			this._post('api/user/check/', this._userLoggedIn, $.toJSON(user));
		},
		_userLoggedIn: function(user) {
			this.options.cx.session('user', user);
			this.options.cx.redirect('#/home');
			this.options.cx.trigger('user-logged-in');
		},
		options: {
			cx: null
		}
	});
	
});
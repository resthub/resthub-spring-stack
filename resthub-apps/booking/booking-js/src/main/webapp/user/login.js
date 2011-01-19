define([ 'require', 'dao/user.dao', 'resthub.controller', 'jquery.json' ], function(require) {

	$.widget("booking.userLogin", $.ui.controller, {
		_init : function() {
			$.storage.clear();
			var user = {
				// TODO a changer
				//username : this.cx().params.username,
				//password : this.cx().params.password
			};
			require('dao/user.dao').check($.proxy(this, '_userLoggedIn'), $.toJSON(user));
		},
		_userLoggedIn : function(user) {
			$.storage.setJSONItem('user', user);
			location.hash = '#/home';
			$.publish('user-logged-in');
		}
	});

});

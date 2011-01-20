define([ 'require', 'resthub.controller', 'jquery.json' ], function(require) {

	$.widget("booking.userLogin", $.ui.controller, {
		options : {
			user : null
		},
		_init : function() {
			$.storage.clear();
			var self = this;
			require(['dao/user.dao'], function() {
				UserDao.check($.proxy(self, '_userLoggedIn'), $.toJSON(self.options.user));
			});
		},
		_userLoggedIn : function(user) {
			$.storage.setJSONItem('user', user);
			location.hash = '#/home';
			$.publish('user-logged-in');
		}
	});

});

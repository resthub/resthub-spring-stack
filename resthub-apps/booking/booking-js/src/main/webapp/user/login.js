define([ 'require', 'resthub.controller', 'jquery.json' ], function(require) {

	$.widget("booking.userLogin", $.ui.controller, {
		options : {
			template: 'user/login.html'
		},
		_init : function() {
			this._render();
			var self = this;
			$('#formLogin').submit(function() {
				$.storage.clear();
				document.title = 'Login';
				var user = {
					username : $('input[name="username"]').val(),
					password : $('input[name="password"]').val()
				};
				require(['dao/user.dao'], function() {
					UserDao.check($.proxy(self, '_userLoggedIn'), $.toJSON(user));
				});
				return false; 
			});
			
		},
		_userLoggedIn : function(user) {
			$.storage.setJSONItem('user', user);
			route('#/home').run();
			$.publish('user-logged-in');
		}
	});

});

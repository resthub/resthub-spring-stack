define([ 'lib/controller', 'repositories/user.repository' ], function(Controller, UserRepository) {

	return Controller.extend("UserLoginController", {
		template: 'user/login.html',
		
		init : function() {
			this.render();
			var self = this;
			$('#formLogin').submit(function() {
				$.storage.remove('user');
				document.title = 'Login';
				var user = {
					username : $('input[name="username"]').val(),
					password : $('input[name="password"]').val()
				};
				require(['repositories/user.repository'], function() {
					UserRepository.check($.proxy(self, '_userLoggedIn'), $.toJSON(user));
				});
				return false; 
			});
			
		},
		_userLoggedIn : function(user) {
			$.storage.set('user', user);
			$.route('#/home');
			$.publish('user-logged-in');
		}
	});

});

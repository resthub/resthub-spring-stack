define([ 'lib/oauth2controller', 'repositories/user.repository',  ], function(OAuth2Controller, UserRepository) {

	return OAuth2Controller.extend("UserLoginController", {
		template: 'user/login.html',
		user: {},
		
		init : function() {
			this.render();
			var self = this;

			$('#formLogin').submit(function() {
				$.storage.remove('user');
				document.title = 'Login';
				self.user = {
					username : $('input[name="username"]').val(),
					password : $('input[name="password"]').val()
				}; 
				self.getOAuth2token(
						self.user.username,
						self.user.password,
						$.proxy(self, '_authenticateHandler'),
						$.proxy(self, '_authenticateErrorHandler')
				);
				
				return false; 
			});
			
		},
		_authenticateErrorHandler: function(error, details) {
			$.loading(false);
			$.pnotify({pnotify_type:'error', pnotify_text: i18n.notifications.wrongCredentials});
		},
		_authenticateHandler: function() {
			$.storage.set('user', this.user);
			$.route('#/home');
			$.publish('user-logged-in');
		}
	});

});

define([ 'text!user/login.html', 'lib/controller', 'lib/oauth2client', 'repositories/user.repository',  ], function(tmpl, Controller, OAuth2Client, UserRepository) {

	return Controller.extend("UserLoginController", {
		template: tmpl,
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
				OAuth2Client.login(
						self.user.username,
						self.user.password,
						$.proxy(self, '_authenticateHandler'),
						$.proxy(self, '_authenticateErrorHandler')
				);
				
				return false; 
			});
			
		},
		_authenticateErrorHandler: function(error, details) {
			$.pnotify({pnotify_type:'error', pnotify_text: "Bad credentials"});
		},
		_authenticateHandler: function() {
			UserRepository.findByUsername(function(data) {
				$.storage.set('user', data);
				$.route('#/home');
				$.publish('user-logged-in');
			}, this.user.username);
		}
	});

});

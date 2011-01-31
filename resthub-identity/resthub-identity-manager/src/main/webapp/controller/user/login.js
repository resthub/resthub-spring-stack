define([
        'lib/oauth2controller',
        'repositories/user.repository',
        'lib/jqueryui/button'
    ], function(OAuth2Controller, UserRepository) {

	return OAuth2Controller.extend('LoginController', {
		
		template: 'controller/user/login.html',
		
		login: "",
		
		init : function() {
			this.render();
			document.title = 'Login';
			var self = this;
			
			$('#formLogin .submit').button().click(function() {	
				$.storage.remove('oauthToken');
				// Try to authenticate
				self.login = $('input[name="username"]').val();
				self.getOAuth2token(
						self.login,
						$('input[name="password"]').val(),
						$.proxy(self, '_authenticateHandler'),
						$.proxy(self, '_authenticateErrorHandler')
				);
				return false;
			});
		},
		
		_getByLoginHandler: function(data, textStatus, XMLHttpRequest) {
			// Stores user in storage.
			$.storage.set(Constants.USER_KEY, data);
			$.route('#/home');
			$.publish('user-logged-in');
		},
		
		_authenticateHandler: function(token) {
			UserRepository.getAuthenticatedDetails($.proxy(this, '_getByLoginHandler'), this.login);
		},
		
		_authenticateErrorHandler: function(error, details) {
			$.pnotify({pnotify_type:'error', pnotify_text:'Wrong credentials !'});
		}
	});

});

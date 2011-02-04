define([
        'lib/oauth2controller',
        'repositories/user.repository',
        'controller/user/utils',
        'lib/jqueryui/button'
    ], function(OAuth2Controller, UserRepository) {

	return OAuth2Controller.extend('HomeController', {
		
		template: 'controller/user/home.html',
				
		user: null,
		
		init : function() {
			this.user = $.storage.get(Constants.USER_KEY);
			this.render({user:this.user});	
			$.connectLogoutButton();
			document.title = 'Home';
			
			$('#passwordChange input[name=password]').keyup($.proxy(this, '_keyupHandler'));
			$('#passwordChange input[name=confirm]').keyup($.proxy(this, '_keyupHandler'));
			$('#passwordChange .submit').button().click($.proxy(this, '_changePasswordButtonHandler'));
		},
		
		_isPasswordSecured: function(value) {
			var regexp = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,15}$/g;
			return value.match(regexp);
		},
		
		_keyupHandler: function() {
			$('#passwordError').html('');
			$('#confirmError').html('');
			var buttonActive = true;
			var password = $('#passwordChange input[name=password]').val();
			var confirm = $('#passwordChange input[name=confirm]').val();
			if (!this._isPasswordSecured(password)) {
				$('#passwordError').html('Your password isn\'t strong enough: please between 5 and 15 characters,' +
					' contains a lower-case, an upper-case and a digit.');
				buttonActive = false;
			} else if (password != confirm) {
				$('#confirmError').html('The confirmation does not match the password.');
				buttonActive = false;
			}
			$('#passwordChange .submit').button("option", "disabled", !buttonActive);
		},
		
		_changePasswordButtonHandler: function() {
			this.user.password = $('#passwordChange input[name=password]').val();
			UserRepository.changePassword($.proxy(this, '_passwordChangedHandler'), $.toJSON(this.user));
		},
		
		_passwordChangedHandler: function(data, textStatus, XMlHttpRequest) {
			$.pnotify("Password successfully changed !");
		}
	});

});

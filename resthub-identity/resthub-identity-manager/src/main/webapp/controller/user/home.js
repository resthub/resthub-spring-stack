define([
        'lib/oauth2controller',
        'repositories/user.repository',
        'controller/user/utils',
        'lib/jqueryui/button'
    ], function(OAuth2Controller, UserRepository) {

	/**
	 * Class HomeController
	 * 
	 * Display user home page, consisting of a password edition form, and administrative links
	 * for admins.
	 */
	return OAuth2Controller.extend('HomeController', {
		
		// -------------------------------------------------------------------------------------------------------------
		// Public attributes

		/**
		 * Controller's template
		 */
		template: 'controller/user/home.html',
				
		/**
		 * Current user.
		 */
		user: null,
		
		// -------------------------------------------------------------------------------------------------------------
		// Private methods

		/**
		 * Indicates wether or not a password is secured enough.
		 * Password must have between 5 and 15 characters, contains at least an upper case letter, a lower case letter,
		 * and a digit.
		 * 
		 * @param value tested password.
		 * @return true if password is secured enough.
		 */
		_isPasswordSecured: function(value) {
			var regexp = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{5,15}$/g;
			return value.match(regexp);
		}, // _isPasswordSecured().
		
			// ---------------------------------------------------------------------------------------------------------
			// UI handlers

		/**
		 * Handler triggered by key press on password text boxes.
		 * 
		 * Checks the password security level, and if relevant, the confirmation equality.
		 * Displays error if necessary.
		 */
		_keyupHandler: function() {
			$('#passwordError').html('');
			$('#confirmError').html('');
			var buttonActive = true;
			// Gets value from UI
			var password = $('#passwordChange input[name=password]').val();
			var confirm = $('#passwordChange input[name=confirm]').val();
			if (!this._isPasswordSecured(password)) {
				// Password too weak.
				$('#passwordError').html('Your password isn\'t strong enough: please between 5 and 15 characters,' +
					' contains a lower-case, an upper-case and a digit.');
				buttonActive = false;
			} else if (password != confirm) {
				// Confirmation not equals
				$('#confirmError').html('The confirmation does not match the password.');
				buttonActive = false;
			}
			// Disable submission button
			$('#passwordChange .submit').button("option", "disabled", !buttonActive);
		}, // _keyupHandler().
		
		/**
		 * Send a call to server to change password.
		 * this._passwordChangedHandler() will be invoked on success.
		 */
		_changePasswordButtonHandler: function() {
			// Updates the current user with clear password.
			this.user.password = $('#passwordChange input[name=password]').val();
			// Server call.
			$.loading(true);
			UserRepository.changePassword($.proxy(this, '_passwordChangedHandler'), $.toJSON(this.user));
		}, // _changePasswordButtonHandler().
		
			// ---------------------------------------------------------------------------------------------------------
			// Server handlers
		
		/**
		 * Callback invoked when password has been changed. Displays notification.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_passwordChangedHandler: function(data, textStatus, XMlHttpRequest) {
			$.loading(false);
			$.pnotify("Password successfully changed !");
		}, // _passwordChangedHandler().
		
		// -------------------------------------------------------------------------------------------------------------
		// Constructor

		/**
		 * Constructor.
		 * Gets current user from the local storage.
		 * Renders its template.
		 * Connect UI components.
		 */
		init : function() {
			this.user = $.storage.get(Constants.USER_KEY);
			this.render({user:this.user});	
			$.connectLogoutButton();
			document.title = 'Home';
			
			$('#passwordChange input[name=password]').keyup($.proxy(this, '_keyupHandler'));
			$('#passwordChange input[name=confirm]').keyup($.proxy(this, '_keyupHandler'));
			$('#passwordChange .submit').button().click($.proxy(this, '_changePasswordButtonHandler'));
		} // constructor().
		
	});

});

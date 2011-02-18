define([
        'i18n!nls/labels',
        'lib/oauth2controller',
        'repositories/user.repository',
        'lib/jqueryui/button'
    ], function(i18n, OAuth2Controller, UserRepository) {

	/**
	 * Class LoginController
	 * 
	 * Display a login forms, and redirect to home on success.
	 */
	return OAuth2Controller.extend('LoginController', {
		
		// -------------------------------------------------------------------------------------------------------------
		// Public attributes

		/**
		 * Controller's template
		 */
		template: 'controller/user/login.html',
		
		// -------------------------------------------------------------------------------------------------------------
		// Private attributes

		/**
		 * Inputed login, reuse to get authenticated user details.
		 */
		_login: "",
		
		// -------------------------------------------------------------------------------------------------------------
		// Private methods

			// ---------------------------------------------------------------------------------------------------------
			// UI handlers

		/**
		 * Handler triggered when login form is submitted.
		 * 
		 * Calls server to perform authentication.
		 * this._authenticateHandler() is the success callback, 
		 * this._authenticateErrorHandler() is the error callback, 
		 */
		_submitButtonHandler: function(token) {
			$.storage.remove('oauthToken');
			// Try to authenticate
			this._login = $('input[name="username"]').val();
			$.loading(true);
			this.getOAuth2token(
					this._login,
					$('input[name="password"]').val(),
					$.proxy(this, '_authenticateHandler'),
					$.proxy(this, '_authenticateErrorHandler')
			);
			return false;		
		}, // _submitButtonHandler().
		
			// ---------------------------------------------------------------------------------------------------------
			// Server handlers

		/**
		 * Invoked on authentication error. Displays a notification.
		 * 
		 * @param error Error status sent by the server.
		 * @param details Detailed text message sent by the server.
		 */
		_authenticateErrorHandler: function(error, details) {
			$.loading(false);
			$.pnotify({pnotify_type:'error', pnotify_text: i18n.notifications.wrongCredentials});
		}, // _authenticateErrorHandler().
		
		/**
		 * Callback invoked when authentication succeeded.
		 */
		_authenticateHandler: function() {
			UserRepository.getAuthenticatedDetails($.proxy(this, '_getByLoginHandler'), this._login);
		}, // _authenticateHandler().

		/**
		 * Invoked after login success, and current user retrieval.
		 * Stored user in local storage and run home route.
		 * 
		 * @param data Server's response.
		 * @param textStatus Http response code.
		 * @param XMLHttpRequest Communication object.
		 */
		_getByLoginHandler: function(data, textStatus, XMLHttpRequest) {
			$.loading(false);
			// Stores user in storage.
			$.storage.set(Constants.USER_KEY, data);
			$.route('#/home');
			$.publish('user-logged-in');
		}, // _getByLoginHandler().
		
		// -------------------------------------------------------------------------------------------------------------
		// Constructor

		/**
		 * Constructor
		 * Template rendering, and UI component initialization.
		 */
		init : function() {
			this.render({i18n:i18n});
			document.title = i18n.titles.login;
			$('#formLogin .submit').button().click($.proxy(this, '_submitButtonHandler'));
			// Empty existing notifications.
			$.pnotify_clear();
		} // Constructor.
		
	}); // Class LoginController.

});

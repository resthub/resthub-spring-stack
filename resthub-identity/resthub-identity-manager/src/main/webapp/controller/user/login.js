define([
        'text!controller/user/login.html',
        'i18n!nls/labels',
        'lib/controller',
        'repositories/user.repository',
        'lib/oauth2client',
        'lib/jqueryui/button'
    ], function(tmpl, i18n, Controller, UserRepository, OAuth2Client) {

	/**
	 * Class LoginController
	 * 
	 * Display a login forms, and redirect to home on success.
	 */
	return Controller.extend('LoginController', {
		
		// -------------------------------------------------------------------------------------------------------------
		// Public attributes

		/**
		 * Controller's template
		 */
		template: tmpl,
		
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
			OAuth2Client.logout();
			// Try to authenticate
			this._login = $('input[name="username"]').val();
			$.loading(true);
			OAuth2Client.login(
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

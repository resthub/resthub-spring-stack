define([
        'i18n!nls/labels',
        'lib/oauth2repository', 
        'controller/utils'
    ], function(i18n, OAuth2Repository) {

	/**
	 * Class UserRepository
	 * 
	 * Server calls to manage users.
	 * Adds permissions management to the CRUD functionnalities.
	 * All functions are statics.
	 */
	return OAuth2Repository.extend("UserRepository", {
		
		// -------------------------------------------------------------------------------------------------------------
		// Static attributes

		/**
		 * Server context root.
		 */
		root : 'api/user/',
		
		// -------------------------------------------------------------------------------------------------------------
		// Static inherited methods

		/**
		 * OAuth2 token error management (override of OAuth2Repository method)
		 * Expired tokens trigger logout.
		 * Otherwise, display error notification.
		 * 
		 * @param XMLHttpRequest The communication object.
		 * @param status Text error returned by server.
		 * @param errorThrown Detailed text error returned by server.
		 */
		authorizationError : function(XMLHttpRequest, status, errorThrown) {
			if (status == 'expired_token' || (status == 'invalid_token' && errorThrown == "Unvalid token")) {
				$.pnotify({
					pnotify_text: i18n.errors.sessionExpired,
					pnotify_type: 'error'
				});
				$.logout();
			} else {
				var error = {
						pnotify_title: i18n.titles.serverError,
						pnotify_text: i18n.errors.serverError,
						pnotify_type: 'error'
					};
					error.pnotify_text += errorThrown ? errorThrown : status;
					$.pnotify(error);
			}
		}, // authorizationError().
	
		// -------------------------------------------------------------------------------------------------------------
		// Static methods

			// ---------------------------------------------------------------------------------------------------------
			// User methods

		/**
		 * Gets details on the connected user.
		 * 
		 * @param callback Callback invoked on server response.
		 */
		getAuthenticatedDetails: function(callback) {
			this._get(this.root + 'me', callback);			
		}, // getAuthenticatedDetails().
		
		/**
		 * Change a user's password.
		 * 
		 * @param callback Callback invoked on server response.
		 * @param user The modified user, embedding its clear password.
		 */
		changePassword: function(callback, user) {
			this._post(this.root + 'password/', callback, user);
		},// changePassword().

			// ---------------------------------------------------------------------------------------------------------
			// Admin methods
		
		/**
		 * Gets permissions of a specific user.
		 * 
		 * @param callback Callback invoked on server response.
		 * @param callback Concerned user.
		 */
		getPermissions: function(callback, user) {
			this._get(this.root + 'name/'+user.login+'/permissions', callback);			
		}, // getPermissions().

		/**
		 * Adds a specific permission to a user.
		 * 
		 * @param callback Callback invoked on server response.
		 * @param callback Concerned user.
		 * @param permission Added permission.
		 */
		addPermission: function(callback, user, permission) {
			this._put(this.root + 'name/'+user.login+'/permissions/'+permission, callback);			
		}, // addPermission().
				
		/**
		 * Removes a specific permission for a user.
		 * 
		 * @param callback Callback invoked on server response.
		 * @param callback Concerned user.
		 * @param permission Removed permission.
		 */
		removePermission: function(callback, user, permission) {
			this._delete(this.root + 'name/'+user.login+'/permissions/'+permission, callback);			
		}, // removePermission().
		
		updateMe : function(callback, data, errorCallback, settings) {
			return this._put(this.root + 'me', callback, data, errorCallback, settings);
		}
		
	}, {}); // Class UserRepository
});

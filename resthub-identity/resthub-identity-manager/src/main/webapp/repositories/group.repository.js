define([
        'i18n!nls/labels',
        'lib/oauth2repository', 
        'controller/utils'
    ], function(i18n, OAuth2Repository) {

	/**
	 * Class GroupRepository
	 * 
	 * Server calls to manage groups.
	 * Adds permissions management to the CRUD functionnalities.
	 * All functions are statics.
	 */
	return OAuth2Repository.extend("GroupRepository", {
		
		// -------------------------------------------------------------------------------------------------------------
		// Static attributes

		/**
		 * Server context root.
		 */
		root : 'api/group/',
		
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
			// Admin methods
		
		/**
		 * Gets permissions of a specific group.
		 * 
		 * @param callback Callback invoked on server response.
		 * @param group Concerned group.
		 */
		getPermissions: function(callback, group) {
			this._get(this.root + 'name/'+group.name+'/permissions', callback);			
		}, // getPermissions().

		/**
		 * Adds a specific permission to a group.
		 * 
		 * @param callback Callback invoked on server response.
		 * @param group Concerned group.
		 * @param permission Added permission.
		 */
		addPermission: function(callback, group, permission) {
			this._put(this.root + 'name/'+group.name+'/permissions/'+permission, callback);			
		}, // addPermission().
				
		/**
		 * Removes a specific permission for a group.
		 * 
		 * @param callback Callback invoked on server response.
		 * @param group Concerned group.
		 * @param permission Removed permission.
		 */
		removePermission: function(callback, group, permission) {
			this._delete(this.root + 'name/'+group.login+'/permissions/'+permission, callback);			
		} // removePermission().
		
	}, {}); // Class UserRepository
});

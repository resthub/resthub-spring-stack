define([
        'lib/oauth2repository', 
        'controller/user/utils' 
    ], function(OAuth2Repository) {

	return OAuth2Repository.extend("UserRepository", {

		root : 'api/user/',
		
		authorizationError : function(XMLHttpRequest, textStatus, errorThrown) {
			if (status == 'expired_token') {
				$.logout();
			} else {
				var error = {
						pnotify_title: 'Server problem',
						pnotify_text: 'The action cannot be realized:\n',
						pnotify_type: 'error'
					};
					error.pnotify_text += errorThrown ? errorThrown : textStatus;
					$.pnotify(error);
			}
		},
	
		getAuthenticatedDetails: function(callback, login) {
			this._get(this.root + 'me', callback);			
		},
		
		getPermissions: function(callback, user) {
			this._get(this.root + 'name/'+user.login+'/permissions', callback);			
		},
			
		removePermission: function(callback, user, permission) {
			this._delete(this.root + 'name/'+user.login+'/permissions/'+permission, callback);			
		},

		addPermission: function(callback, user, permission) {
			this._put(this.root + 'name/'+user.login+'/permissions/'+permission, callback);			
		},

		list: function(callback) {
			this._get(this.root, callback);
		},
		
		changePassword: function(callback, user) {
			this._post(this.root + 'password/', callback, user);
		}
	}, {});
});

define([ 'lib/oauth2repository' ], function(OAuth2Repository) {

	return OAuth2Repository.extend("UserRepository", {

		root : 'api/user/',

		findByUsername : function(callback, username) {
			this._get(this.root + 'username/' + username, callback);
		}

	}, {});
});

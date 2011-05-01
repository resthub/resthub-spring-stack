define([ 'lib/oauth2repository' ], function(OAuth2Repository) {

	return OAuth2Repository.extend("UserRepository", {

		root : 'api/user/',

		check : function(callback, data) {
			this._post(this.root + 'check/', callback, data);
		}

	}, {});
});

define([ 'lib/repository' ], function(Repository) {

	return Repository.extend("UserRepository", {

		root : 'api/user/',

		check : function(callback, data) {
			this._post(this.root + 'check/', callback, data);
		}

	}, {});
});

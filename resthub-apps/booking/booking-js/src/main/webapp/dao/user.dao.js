define([ 'resthub.dao' ], function(Dao) {

	Dao.extend("UserDao", {

		root : 'api/user/',

		check : function(callback, data) {
			this._post(this.root + 'check/', callback, data);
		}

	}, {});
});

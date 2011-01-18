define([ 'jquery.dao' ], function(CrudDao) {
	return CrudDao.define("UserDao").supplement({
		root : 'api/user/',

		check : function(callback, data) {
			this._post(this.root + 'check/', callback, data);
		}

	});
});

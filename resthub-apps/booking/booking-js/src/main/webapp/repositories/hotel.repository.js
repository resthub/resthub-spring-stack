define([ 'lib/repository' ], function(Repository) {

	return Repository.extend("HotelRepository", {

		root : 'api/hotel/',

		find : function(callback, val, page, size) {
			var url = this.root + 'search?page=' + page + '&size=' + size;
			if (val) {
				url = url + '&q=' + val;
			}
			this._get(url, callback);
		}
	}, {});
});

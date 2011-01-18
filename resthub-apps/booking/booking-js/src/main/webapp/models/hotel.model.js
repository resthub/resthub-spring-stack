define([ 'jquery.model' ], function() {

	Model.extend("Hotel", {
		init : function() {
			this.root = 'api/hotel/';
		},
		find : function(callback, val, page, size) {
			var url = this.root + 'search?page=' + page + '&size=' + size;
			if (val) {
				url = url + '?q=' + val;
			}
			this._get(url, callback);
		}
	}, {});
});
define(['jquery.model'], function() {
	
	Model.extend("Hotel", {
		find: function(callback, val, page, size) {
			var url = 'api/hotel/search?page=' + page + '&size=' + size;
			if(val) {
				url = url + '?q=' + val;
			}
			this._get(url, callback);
		 }
	}, {});
});
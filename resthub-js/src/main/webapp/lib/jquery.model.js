define(['jquery','jquery.class'], function(jQuery) {
	$.Class.extend("Model", {
		
		init: function(name){
		    this.name = name;
		},
		_post: function _post(url, callback, data) {
			this._ajax(url, callback, 'POST', data);
		},

		_get: function _get(url, callback) {
			this._ajax(url, callback, 'GET', null);
		},

		_put: function _put(url, callback, data) {
			this._ajax(url, callback, 'PUT', data);
		},

		_delete: function _delete(url, callback) {
			this._ajax(url, callback, 'DELETE', null);
		},

		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax: function(url, callback, type, data) {
			$.ajax({
				url: url,
				dataType: this.defaults.dataType,
				contentType: this.defaults.contentType,
				type: type,
				data: data,
				success: callback
			});
		},
		defaults: {
			dataType: 'json',
			contentType: 'application/json; charset=utf-8'
		}
	}, {});
});
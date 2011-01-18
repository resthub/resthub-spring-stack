define([ 'jquery', 'jquery.class' ], function($, Class) {

	return Class.extend("Model", 
		/* @static */ {
			defaults : {
				dataType : 'json',
				contentType : 'application/json; charset=utf-8'
			}
		},
		/* @prototype */ {
		init : function() {
			this.root = '';
		},
		read : function(callback, id) {
			this._get(this.root + id, callback);
		},
		remove : function(callback, id) {
			this._delete(this.root + id, callback);
		},
		save : function(callback, data) {
			this._post(this.root, callback, data);
		},
		update : function(callback, id, data) {
			this._put(this.root + id, callback, data);
		},

		_post : function _post(url, callback, data) {
			this._ajax(url, callback, 'POST', data);
		},

		_get : function _get(url, callback) {
			this._ajax(url, callback, 'GET', null);
		},

		_put : function _put(url, callback, data) {
			this._ajax(url, callback, 'PUT', data);
		},

		_delete : function _delete(url, callback) {
			this._ajax(url, callback, 'DELETE', null);
		},

		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax : function(url, callback, type, data) {
			$.ajax({
				url : url,
				dataType : this.Class.defaults.dataType,
				contentType : this.Class.defaults.contentType,
				type : type,
				data : data,
				success : callback
			});
		},

	});
});

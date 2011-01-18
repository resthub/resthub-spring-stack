define([ 'jquery', 'jquery.class' ], function($, Class) {

	return Class.extend("CrudDao", 
	/* @static */ {
		defaults : {
			dataType : 'json',
			contentType : 'application/json; charset=utf-8'
		},

		/**
		 * Just a helper that improves extension creation readability
		 * @return a dummy object (containing only the supplement function) 
		 * to complete the chain to make a proper "extend" with all needed data.
		 */
		define: function (className) {
			return {
				supplement: function(statics) {
					return Dao.extend(className, statics, {});
			}
		},
		// default root
		root: '',
		//singleton methods
		init : function() {
			this.root = this.root||'';
		},
		read : function(callback, id) {
			return this._get(this.root + id, callback);
		},
		remove : function(callback, id) {
			return this._delete(this.root + id, callback);
		},
		save : function(callback, data) {
			return this._post(this.root, callback, data);
			
		},
		update : function(callback, id, data) {
			return this._put(this.root + id, callback, data);
		},

		//will probably not be overriden
		_post : function _post(url, callback, data) {
			this._ajax(url, callback, 'POST', data);
			return this;
		},

		_get : function _get(url, callback) {
			this._ajax(url, callback, 'GET', null);
			return this;
		},

		_put : function _put(url, callback, data) {
			this._ajax(url, callback, 'PUT', data);
			return this;
		},

		_delete : function _delete(url, callback) {
			this._ajax(url, callback, 'DELETE', null);
			return this;
		},

		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax : function(url, callback, type, data) {
			$.ajax({
				url : url,
				dataType : this.defaults.dataType,
				contentType : this.defaults.contentType,
				type : type,
				data : data,
				success : callback
			});
		}
	});
});

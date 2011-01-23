define([ 'jquery', 'class' ], function($, Class) {

	/**
	 * Repository class are designed to send ajax requests in order to retreive/send data from/to server
	 * Since they are stateless, they only define static vars and functions
	 * Default data format is json
	 * 
	 * Usage :
	 * 
	 * 		Repository.extend("BookingRepository", {
	 *			root : 'api/booking/'
	 *		}, {});
	 *
	 *  	BookingRepository.read(callback, id);
	 *  
	 *  Be carefull about 2 points :
	 *   - Don't forget the second pair of {} in your repository declaration, it means that vars and functions declared in
	 *     the first one are static. Read Class JSdoc for more details
	 *   - you may need to use $.proxy(this, 'callback') instead just callback if you use "this" object in your callback
	 */
	return Class.extend("Repository", {

		defaults : {
			dataType : 'json',
			contentType : 'application/json; charset=utf-8'
		},

		/**
		 * Default URL root for ajax requests
		 * For example, 'api/users'
		 **/
		root : '',
		
		init : function() {
			this.root = this.root || '';
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
	}, {});
});

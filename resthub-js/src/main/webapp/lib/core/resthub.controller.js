/**
 * Resthub-controller is a generic javascript controller for resthub applications.
 * It provides utility functions for basic opérations.
 */

(function($)
{
	var resthubController =
	{
		_create: function() {
		// TODO
		},

		_init: function() {
		// TODO
		},

		_post: function _post(url, callback, data) {
			this._ajax(url, callback, 'post', data);
		},

		_get: function _get(url, callback) {
			this._ajax(url, callback, 'get', null);
		},

		_put: function _put(url, callback, data) {
			this._ajax(url, callback, 'put', data);
		},

		_delete: function _delete(url, callback) {
			this._ajax(url, callback, 'delete', null);
		},

		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax: function(url, callback, type, data) {
			$.ajax({
				url: url,
				dataType: this.options.dataType,
				contentType: this.options.contentType,
				type: type,
				data: data,
				success: $.proxy( callback , this )
			});
		},
		_set: function(key, value) {
			this.options[key] = value;
		},
		options: {
			dataType: 'json',
			contentType: 'application/json; charset=utf-8'
		}
	};

	$.widget("resthub.resthubController", resthubController);
	
})(jQuery);
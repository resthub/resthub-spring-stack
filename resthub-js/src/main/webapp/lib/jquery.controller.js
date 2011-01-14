/**
 * Resthub-controller is a generic javascript controller for resthub applications.
 * It provides utility functions for basic op�rations.
 */
define(['jquery','jqueryui/widget'], function(jQuery) {

(function( $, undefined ) {

	$.widget("ui.controller", {
		_create: function() {
		// TODO
		},

		_init: function() {
		// TODO
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
				dataType: this.options.dataType,
				contentType: this.options.contentType,
				type: type,
				data: data,
				success: $.proxy( callback , this )
			});
		},
		
		options: {
			dataType: 'json',
			contentType: 'application/json; charset=utf-8'
		}
	});
	
})(jQuery);
	
});
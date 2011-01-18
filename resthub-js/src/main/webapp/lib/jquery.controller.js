/**
 * Resthub-controller is a generic javascript controller for resthub
 * applications. It provides utility functions for basic op�rations.
 */
define(['jquery','jqueryui/widget'], function(jQuery) {

(function( $, undefined ) {

	$.widget("ui.controller", {
		
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
		
		/**
		 * Render current widget with the template specified in
		 * this.options.template. If none is defined, it used a view with the
		 * same name than the controller
		 */
		_render: function(data) {
			if(typeof(this.options.template)=='undefined') {
				this.element.render('./' + this.widgetName + '.html', data);
			} else {
				this.element.render(this.options.template, data);
			}	
		},
		
		/**
		 * Shortcut for context
		 */
		cx: function() {
			return this.options.cx;
		},
		
		/**
		 * Shortcut for template
		 */
		template: function() {
			return this.options.template;
		},

		
		options: {
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			/** Context **/
			cx : null,
			template: ''
		}
	});
	
})(jQuery);
	
});
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

		_post: function(url, widget, callback, data) {
			this._ajax(url, widget, callback, 'post', data);
		},

		_get: function(url, widget, callback) {
			this._ajax(url, widget, callback, 'get', null);
		},

		_put: function(url, widget, callback) {
			this._ajax(url, widget, callback, 'put', null);
		},

		_delete: function(url, widget, callback) {
			this._ajax(url, widget, callback, 'delete', null);
		},

		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax: function(url, widget, callback, type, data) {

			$.ajax({
				url: url,
				dataType: this.options.dataType,
				contentType: this.options.contentType,
				type: type,
				data: data,
				success: function(data) {
					(widget)[callback](data);
				},
				error: function(request, status, error) {
					switch (request.status) {
						case 403:
							$.pnotify('Forbidden (' + request.status + ').');
							break;
						case 404:
							$.pnotify('No data found (' + request.status + ').');
							break;
						case 500:
							$.pnotify('An error occurred during Ajax request (' + request.status + ').');
							if( error != undefined ) {console.log(error);}
							break;
						default:
							$.pnotify('An error occurred during Ajax request (' + request.status + ').');
							break;
					}
				}
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
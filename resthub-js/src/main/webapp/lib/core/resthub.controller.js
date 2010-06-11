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
		
		/**
		 * Perform basic ajax request and call your widget back.
		 */
		_ajax: function(url, widget, callback) {
			if(url)
			{
				$.ajax({
					url: url,
					dataType: this.options.format,
					type: this.options.type,
					success: function(data) {
						(widget)[callback](data);
					},
					error: function(request, status, error) {
						switch (request.status) {
							case 404:
								console.log('No data found (' + request.status + ').');
								break;
							case 500:
								console.log('An error occurred during Ajax request (' + request.status + ').');
								if( error != undefined ) { console.log(error); }
								break;
							default:
								console.log('An error occurred during Ajax request (' + request.status + ').');
								break;
						}
					}
				});
			}
		},

		options: {
			url: null,
			type: 'get',
			format: 'json'
		}
	};

	$.widget("resthub.resthubController", resthubController);
})(jQuery);
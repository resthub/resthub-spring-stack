/**
 * Resthub-route is a generic widget to execute routes in a Resthub app.
 * If the url param is given, it executes an ajax request before calling the callback function.
 * Otherwise, it only call the callback function.
 */

(function($)
{
	var restController =
	{
		_init: function() {

			if(this.options.jsFile == null) {
				console.log("Unable to find javascript file to call...");
				// TODO : Something like try/catch -> exit
			}
			if(this.options.callbackFunction == null) {
				console.log("Unable to find callback function to call...");
				// TODO : Something like try/catch -> exit
			}

			if(this.options.url != null) // Ajax request to perform : data receives the response
			{
				var self = this;
				$.ajax({
					url: this.options.url,
					dataType: this.options.format,
					type: this.options.type,
					success: function(data) {
						self.callWidget(data);
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
			else // No Ajax request in this route : data receives options.data
			{
				this.callWidget(this.options.data);
			}
		},

		/*
		 * Call the widget identified by callbackFunction on 'this.element' DOM element
		 * This widget receives data and context
		 */
		callWidget: function (data) {
			var self = this;
			dominoes(this.options.jsFile, function() {
				console.log('Data parameter : ' + $.toJSON(data));
				$(self.element)[self.options.callbackFunction]({data: data, context: self.options.context});
			});
		},

		options: {
			url: null,
			type: 'get',
			format: 'json',
			jsFile: null,
			callbackFunction: null,
			data: null,
			context: null
		}
	};
	
	$.widget("resthub.restController", restController);
})(jQuery);
/**
 * Resthub-route is a generic widget to execute routes in a Resthub app.
 * If the url param is given, it executes an ajax request before calling the callback function.
 * Otherwise, it only call the callback function.
 */

(function($){
	$.executeRoute = function(params) {
		params = $.extend({
			bloc: '#content',
			url: null,
			type: 'get',
			format: 'json',
			jsFile: null,
			callbackFunction: null,
			data: null,
			context: null
		}, params);

		if(params.jsFile == null) {
			console.log("Unable to find javascript file to call...");
			// TODO : Something like try/catch -> exit
		}
		if(params.callbackFunction == null) {
			console.log("Unable to find callback function to call...");
			// TODO : Something like try/catch -> exit
		}

		if(params.url != null) // Ajax request to perform : data receives the response
		{
			dominoes(params.jsFile, function() {
				$.ajax({
					url: params.url,
					dataType: params.format,
					type: params.type,
					success: function(data) {
						console.log($.toJSON(data));
						var stringToCall = '$("'+ params.bloc +'").'+ params.callbackFunction +'({data: data, context: params.context})';
						console.log(stringToCall);
						eval(stringToCall);
					}
				});
			});
		}
		else // No Ajax request in this route : data receives params.data
		{
			dominoes(params.jsFile, function() {
				console.log($.toJSON(params.data));
				var stringToCall = '$("'+ params.bloc +'").'+ params.callbackFunction +'({data: params.data, context: params.context})';
				console.log(stringToCall);
				eval(stringToCall);
			});
		}
	}
})(jQuery);
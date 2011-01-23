define(['jquery', 'jquery.pnotify'], function(jQuery) {

	//--- Ajax request with errors management ---//
	var old_ajax = $.ajax;
	var resthubSettings = {
		error: function(request, status, error) {
			/*
			 * If an error message is specified (other than html), it is displayed.
			 * Otherwise, a generic message is displayed according to the error code.
			 */
			if(request.responseText && request.responseText.substring(0, 6) != '<html>' && request.status >= 300)
			{
				$.pnotify ({
					pnotify_title: 'Error ' + request.status,
					pnotify_text: request.responseText,
					pnotify_type: 'error'
				});
			}
			else
			{
				if (request.status < 400) {
					$.pnotify ({
						pnotify_title: 'Information',
						pnotify_text: request.statusText
					});
				}
				if(request.status >= 400 && request.status < 500) {
					$.pnotify ({
						pnotify_title: 'Client error ' + request.status,
						pnotify_text: request.statusText,
						pnotify_type: 'error'
					});
				}
				if(request.status >= 500 && request.status < 600) {
					$.pnotify ({
						pnotify_title: 'Server error ' + request.status,
						pnotify_text: request.statusText,
						pnotify_type: 'error'
					});
				}
			}
		}
	};
	$.ajax = function (origSettings) {
		var s = jQuery.extend(true, {}, resthubSettings, origSettings);
		old_ajax(s);
	};

});
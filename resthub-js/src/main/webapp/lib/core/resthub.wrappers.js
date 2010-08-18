/*--- RESThub Wrappers ---*/

(function($) {

	//--- Ajax request with errors management ---//
	var old_ajax = $.ajax;
	var resthubSettings = {
		error: function(request, status, error) {
			/*
			 * If an error message is specified (other than html), it is displayed.
			 * Otherwise, a generic message is displayed according to the error code.
			 */
			if(request.responseText && request.responseText.substring(0, 6) != '<html>')
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

	//--- Lazy loading using dominoes for sammy ---//
	var sammyOriginalVerbs = {
		get : Sammy.Application.prototype.get,
		set : Sammy.Application.prototype.set,
		post : Sammy.Application.prototype.post,
		del : Sammy.Application.prototype.del,
		any : Sammy.Application.prototype.any
	};

	//--- TODO : improve code below which is very dirty, don't know how to do that better ---//
	Sammy.Application.prototype['get'] = function(path, callback, jsFile) {

		var realCallback = function () {
			return function() {
				if(jsFile == undefined)
				{
					callback.apply(this, arguments);
				}
				else
				{
					var self = this;
					var args = arguments;
					dominoes(jsFile, function () {
						return function() {
							callback.apply(this, arguments);
							this.trigger('event-context-after-lazy', {
								context: this
							});
						}.apply(self, args)
					});
				}
			}.apply(this, arguments);
		}

		sammyOriginalVerbs['get'].apply( this, [path, realCallback] );
	}

	Sammy.Application.prototype['set'] = function(path, callback, jsFile) {

		var realCallback = function () {
			return function() {
				if(jsFile == undefined)
				{
					callback.apply(this, arguments);
				}
				else
				{
					var self = this;
					var args = arguments;
					dominoes(jsFile, function () {
						return function() {
							callback.apply(this, arguments);
							this.trigger('event-context-after-lazy', {
								context: this
							});
						}.apply(self, args)
					});
				}
			}.apply(this, arguments);
		}

		sammyOriginalVerbs['set'].apply( this, [path, realCallback] );
	}

	Sammy.Application.prototype['post'] = function(path, callback, jsFile) {

		var realCallback = function () {
			return function() {
				if(jsFile == undefined)
				{
					callback.apply(this, arguments);
				}
				else
				{
					var self = this;
					var args = arguments;
					dominoes(jsFile, function () {
						return function() {
							callback.apply(this, arguments);
							this.trigger('event-context-after-lazy', {
								context: this
							});
						}.apply(self, args)
					});
				}
			}.apply(this, arguments);
		}

		sammyOriginalVerbs['post'].apply( this, [path, realCallback] );
	}

	Sammy.Application.prototype['del'] = function(path, callback, jsFile) {
		
		var realCallback = function () {
			return function() {
				if(jsFile == undefined)
				{
					callback.apply(this, arguments);
				}
				else
				{
					var self = this;
					var args = arguments;
					dominoes(jsFile, function () {
						return function() {
							callback.apply(this, arguments);
							this.trigger('event-context-after-lazy', {
								context: this
							});
						}.apply(self, args)
					});
				}
			}.apply(this, arguments);
		}

		sammyOriginalVerbs['del'].apply( this, [path, realCallback] );
	}

	Sammy.Application.prototype['any'] = function(path, callback, jsFile) {

		var realCallback = function () {
			return function() {
				if(jsFile == undefined)
				{
					callback.apply(this, arguments);
				}
				else
				{
					var self = this;
					var args = arguments;
					dominoes(jsFile, function () {
						return function() {
							callback.apply(this, arguments);
							this.trigger('event-context-after-lazy', {
								context: this
							});
						}.apply(self, args)
					});
				}
			}.apply(this, arguments);
		}

		sammyOriginalVerbs['any'].apply( this, [path, realCallback] );
	}

})(jQuery);
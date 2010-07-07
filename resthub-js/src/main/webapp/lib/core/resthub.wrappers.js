/*--- RESThub Wrappers ---*/

(function($) {

	//--- Ajax request with errors management ---//
	var old_ajax = $.ajax;
	var resthubSettings = {
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
					if( error != undefined ) {
						console.log(error);
					}
					break;
				default:
					$.pnotify('An error occurred during Ajax request (' + request.status + ').');
					break;
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
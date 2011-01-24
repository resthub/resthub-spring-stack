/*
 * RESThub publish/subscribe plugin
 * Implements a simple event bus in order to allow low coupling in you application
 * 
 * Based jQuery Tiny Pub/Sub - v0.3 - 11/4/2010
 * http://benalman.com/
 *
 * Copyright (c) 2010 "Cowboy" Ben Alman
 * Dual licensed under the MIT and GPL licenses.
 * http://benalman.com/about/license/
 */
define(['jquery'], function (jQuery) {
	(function($){
	  
	  var o = $({});
	  
	  /**
	   * Define an event handler for this eventType listening on th event bus
	   *
	   * subscribe( eventType, handler(args) )
	   * @param {String} eventType A string that identify your custom javaScript event type
	   * @param {function} handler(args) function to execute each time the event is triggered, with
	   * 
	   */
	  $.subscribe = function() {
		
		var type = arguments[0];
		var callback = arguments[1];
		// Remove uneeded event argument
		var new_callback = function() {
			callback.apply(this, Array.prototype.slice.call(arguments, 1));
		};
	    o.bind.apply( o, [type, new_callback] );
	  };
	  
	  /**
	   * Remove a previously-defined event handler for the matching eventType
	   * 
	   * @param {String} eventType A string that identify your custom javaScript event type
	   */
	  $.unsubscribe = function() {
	    o.unbind.apply( o, arguments );
	  };
	  
	  /**
	   * Publish an event in the event bus
	   * 
	   * @param {String} eventType A string that identify your custom javaScript event type
	   * @param {Array} extraParameters  Additional parameters to pass along to the event handler
	   */
	  $.publish = function() {
	    o.trigger.apply( o, arguments );
	  };
	  
	})(jQuery);
});
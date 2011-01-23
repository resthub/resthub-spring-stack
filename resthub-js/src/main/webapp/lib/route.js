define(['jquery', 'pubsub'], function (jQuery) {
	(function($) {
		
		/**
		 * Define or run a route depending parameters.
		 * A route is defined by a location hash, that will trigger the callback passed in parameter
		 * 
		 * On recent browser, the hashchange event is used. On other browser, a timer check if location.hash has changed
		 * or not in order to determine what route should be runned
		 * 
		 * Define a route with the matching callback
		 * $.route('#/route1', function() {
		 * 	console.log('Run route 1');
		 * });
		 * 
		 * Define a route with parameter
		 * $.route('#/route2/:id', function(params) {
		 * 	console.log('Run route 2 with parameter id = ' + params.id);
		 * });
		 * 
		 * Run a route
		 * When a route is runned, thet run-route event is dispatched (could be catched thanks to $.subscribe())
		 * $.route('#/route1');
		 * 
		 */
		$.route = function() {
			
			if(arguments.length == 0 || arguments.length > 2) {
				console.error('Wrong number of arguments, $.route take 1 or 2 argument');
				return;
			}
			
			var path = arguments[0];
			
			// Save the original path
			var real_path = path;
			
			/* remove trailing slash from path if it exists */
			if(path[path.length-1]=='/' && path!= '#/') {
				path = path.substring(0, path.length - 1);
			}
			
			if(path=='' || path=='#') {
				path = '#/';
			}
			
			var args = {};
			// Run route
			if(arguments.length == 1) {
				
				// Check route pattern matching
				console.debug("Begin matching tests for route " + path);
				for(registered_route in $.route.routes){
					
					var path_pattern = '^' + registered_route.replace(/:\w+/g,'(\\w+)') + '$';
					var path_regexp = new RegExp(path_pattern); 
					var path_parts = path.match(path_regexp);
					
					// If path match
					if(path_parts) {
						console.debug("Found a matching between " + path + ' and ' + registered_route );
						var registered_route_pattern = '^' + registered_route.replace(/:\w+/g,':(\\w+)') + '$';
						var registered_route_regexp = new RegExp(registered_route_pattern);
						var registered_route_parts = registered_route.match(registered_route_regexp);
						
						if(path_parts.length != registered_route_parts.length) {
							console.error('Path and registered_route habe not the same part count !!!');
						}
						
						for(var i=1; i<path_parts.length; i++){
							args[registered_route_parts[i]] = path_parts[i];
						}		
						path = registered_route;
					}
					
				}
				
				if(typeof $.route.routes[path] == 'undefined'){
					console.info('No route registered for path = ' + path);
					return;
				}
				
				var callbacks = $.route.routes[path];
				for(var i=0; i<callbacks.length; i++){
					callbacks[i](args);  
				}			
				location.hash = real_path;
				$.publish('run-route', real_path);
				console.debug('Run route ' + arguments[0]);
				
			// Register route
			} else if(arguments.length == 2) {
				var callback = arguments[1];
				
				if(typeof $.route.routes[path] == 'undefined'){
					$.route.routes[path] = new Array();
				}
				$.route.routes[path].push(callback);
				console.debug('Route ' + path + ' registered !');
			} 		
		};
		
		$.route.routes = {};
		
		$.route.dispacher = {};
		$.route.dispacher._last = '';
		
		$.route.dispacher._onhashchange = function() {
		    if($.route.dispacher._last != location.hash){
		    	$.route.dispacher._last = location.hash;
		        $.route(location.hash);
		    }
		};
		
		if ("onhashchange" in window) {
			$(window).bind( 'hashchange', $.route.dispacher._onhashchange);
			console.debug('Routing is using native window.hashchange event');
		} else {
			console.debug('No support for window.hashchange, so routing is emulated with a timer');
			setInterval($.route.dispacher._onhashchange, 100);
		}
		
	})(jQuery);
});
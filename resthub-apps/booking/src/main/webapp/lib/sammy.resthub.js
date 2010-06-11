(function($) {

  Sammy = Sammy || {};

  Sammy.Resthub = function() {

  var PATH_REPLACER = "([^\/]+)",
      PATH_NAME_MATCHER = /:([\w\d]+)/g,
      QUERY_STRING_MATCHER = /\?([^#]*)$/,
      // mainly for making `arguments` an Array
      _makeArray = function(nonarray) { return Array.prototype.slice.call(nonarray); },
      // borrowed from jQuery
      _isFunction = function( obj ) { return Object.prototype.toString.call(obj) === "[object Function]"; },
      _isArray = function( obj ) { return Object.prototype.toString.call(obj) === "[object Array]"; },
      _decode = decodeURIComponent,
      loggers = [],
	  _routeWrapper = function(verb) {
	  return function(path, callback, jsFile) { return this.route.apply(this, [verb, path, callback, jsFile]); };
	};

	$.extend(Sammy.Application.prototype, 
	{
		// Alias for route('get', ...)
		get: _routeWrapper('get'),

		// Alias for route('post', ...)
		post: _routeWrapper('post'),

		// Alias for route('put', ...)
		put: _routeWrapper('put'),

		// Alias for route('delete', ...)
		del: _routeWrapper('delete'),

		// Alias for route('any', ...)
		any: _routeWrapper('any'),

		route: function(verb, path, callback, jsFile) {
		  // console.log('nouvelle route');
		  var app = this, param_names = [], add_route;

		  // if the method signature is just (path, callback)
		  // assume the verb is 'any'
		  if (!callback && _isFunction(path)) {
			path = verb;
			callback = path;
			verb = 'any';
		  }

		  verb = verb.toLowerCase(); // ensure verb is lower case

		  // if path is a string turn it into a regex
		  if (path.constructor == String) {

			// Needs to be explicitly set because IE will maintain the index unless NULL is returned,
			// which means that with two consecutive routes that contain params, the second set of params will not be found and end up in splat instead of params
			// https://developer.mozilla.org/en/Core_JavaScript_1.5_Reference/Global_Objects/RegExp/lastIndex
			PATH_NAME_MATCHER.lastIndex = 0;

			// find the names
			while ((path_match = PATH_NAME_MATCHER.exec(path)) !== null) {
			  param_names.push(path_match[1]);
			}
			// replace with the path replacement
			path = new RegExp("^" + path.replace(PATH_NAME_MATCHER, PATH_REPLACER) + "$");
		  }
		  // lookup callback
		  if (typeof callback == 'string') {
			callback = app[callback];
		  }

		  add_route = function(with_verb) {
			var r = {verb: with_verb, path: path, jsFile: jsFile, callback: callback, param_names: param_names};
			// add route to routes array
			app.routes[with_verb] = app.routes[with_verb] || [];
			// place routes in order of definition
			app.routes[with_verb].push(r);
		  };

		  if (verb === 'any') {
			$.each(this.ROUTE_VERBS, function(i, v) { add_route(v); });
		  } else {
			add_route(verb);
		  }

		  // return the app
		  return this;
		},

		runRoute: function(verb, path, params, target) {
		  // console.log('nouveau run route');
		  var app = this,
			  route = this.lookupRoute(verb, path),
			  context,
			  wrapped_route,
			  arounds,
			  around,
			  befores,
			  before,
			  callback_args,
			  final_returned;

		  this.log('runRoute', [verb, path].join(' '));
		  this.trigger('run-route', {verb: verb, path: path, params: params});
		  if (typeof params == 'undefined') { params = {}; }

		  $.extend(params, this._parseQueryString(path));

		  if (route) {
			this.trigger('route-found', {route: route});
			// pull out the params from the path
			if ((path_params = route.path.exec(this.routablePath(path))) !== null) {
			  // first match is the full path
			  path_params.shift();
			  // for each of the matches
			  $.each(path_params, function(i, param) {
				// if theres a matching param name
				if (route.param_names[i]) {
				  // set the name to the match
				  params[route.param_names[i]] = _decode(param);
				} else {
				  // initialize 'splat'
				  if (!params.splat) { params.splat = []; }
				  params.splat.push(_decode(param));
				}
			  });
			}

			// set event context
			context  = new this.context_prototype(this, verb, path, params, target);
			// ensure arrays
			arounds = this.arounds.slice(0);
			befores = this.befores.slice(0);
			// set the callback args to the context + contents of the splat
			callback_args = [context].concat(params.splat);
			// wrap the route up with the before filters
			wrapped_route = function() {
			  var returned;
			  while (befores.length > 0) {
				before = befores.shift();
				// check the options
				if (app.contextMatchesOptions(context, before[0])) {
				  returned = before[1].apply(context, [context]);
				  if (returned === false) { return false; }
				}
			  }
			  app.last_route = route;
			  context.trigger('event-context-before', {context: context});

			  if(route.jsFile == undefined)
			  {
				  returned = route.callback.apply(context, callback_args);
			  }
			  else
			  {
				  // console.log('Passage dans le dominoes...');
				  dominoes(route.jsFile, function() {
					  returned = route.callback.apply(context, callback_args);
				  });
			  }

			  context.trigger('event-context-after', {context: context});
			  return returned;
			};
			$.each(arounds.reverse(), function(i, around) {
			  var last_wrapped_route = wrapped_route;
			  wrapped_route = function() { return around.apply(context, [last_wrapped_route]); };
			});
			try {
			  final_returned = wrapped_route();
			} catch(e) {
			  this.error(['500 Error', verb, path].join(' '), e);
			}
			return final_returned;
		  } else {
			return this.notFound(verb, path);
		  }
		}
	});
  };

})(jQuery);
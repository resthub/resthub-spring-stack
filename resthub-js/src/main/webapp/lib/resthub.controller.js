/**
 * Resthub-controller is a generic javascript controller for resthub
 * applications. It provides utility functions for basic op�rations.
 */
define([ 'jquery', 'jquery.class', 'jquery.ejs' ], function($, Class) {

	return Class.extend("Controller", {


		// used to remove the controller from the name
		_underscoreAndRemoveController : function(className) {
			var str = className.replace("jQuery.", "").replace( /\./g, '_').replace(/_?controllers?/ig, "");
			return str.replace(/::/, '/').replace(/([A-Z]+)([A-Z][a-z])/g, '$1_$2').replace(/([a-z\d])([A-Z])/g, '$1_$2').replace(/([a-z\d])([A-Z])/g, '_').toLowerCase();
		},

		init : function() {
			// if you didn't provide a name, or are controller,
			// don't do anything
			if (!this.shortName || this.fullName == "jQuery.Controller") {
				return;
			}
			// cache the underscored names
			this._fullName = this._underscoreAndRemoveController(this.fullName);
			this._shortName = this._underscoreAndRemoveController(this.shortName);

			var controller = this, pluginname = this.pluginName || this._fullName, funcName, forLint;

			// create jQuery plugin
			if (!$.fn[pluginname]) {
				$.fn[pluginname] = function(options) {

					var args = $.makeArray(arguments),
					// if the arg is a method on this controller
					isMethod = typeof options == "string"
							&& $.isFunction(controller.prototype[options]), meth = args[0];
					this.each(function() {
						// check if created
						var controllers = $.data(this, "controllers"),
						// plugin is actually the controller
						// instance
						plugin = controllers && controllers[pluginname];

						if (plugin) {
							if (isMethod) {
								// call a method on the
								// controller with the remaining
								// args
								plugin[meth].apply(plugin, args.slice(1));
							} else {
								// call the plugin's update
								// method
								plugin.update.apply(plugin, args);
							}

						} else {
							// create a new controller instance
							controller.newInstance.apply(controller, [ this ].concat(args));
						}
					});
					// always return the element
					return this;
				};
			}
		}

		}, {
			
			template: '',
			
			setup: function( element, options ) {
				this.element = $(element);
			},
			
			/**
			 * Renders current widget with the template specified in
			 * this.options.template. If none is defined, it used a
			 * view with the same name of the controller
			 */
			render : function(data) {
				if (typeof (this.template) == 'undefined') {
					this.element.render('./' + this.widgetName + '.html', data);
				} else {
					this.element.render(this.template, data);
				}
			}
		});

});

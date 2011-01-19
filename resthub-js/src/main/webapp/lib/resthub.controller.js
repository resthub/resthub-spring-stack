/**
 * Resthub-controller is a generic javascript controller for resthub
 * applications. It provides utility functions for basic op�rations.
 */
define(['jquery', 'jqueryui/widget'], function($) {
	$.widget("ui.controller", {
		/**
		 * Renders current widget with the template specified in
		 * this.options.template. If none is defined, it used a view with the
		 * same name of the controller
		 */
		_render: function(data) {
			if(typeof(this.options.template)=='undefined') {
				this.element.render('./' + this.widgetName + '.html', data);
			} else {
				this.element.render(this.options.template, data);
			}	
		},
		
		/**
		 * Shortcut for template
		 */
		template: function() {
			return this.options.template;
		},
		
		options: {
			dataType: 'json',
			contentType: 'application/json; charset=utf-8',
			template: ''
		}
	});

});

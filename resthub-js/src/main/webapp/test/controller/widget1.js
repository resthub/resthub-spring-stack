define(['jquery.controller', 'jquery.ejs'], function() {
	(function($) {
		$.widget("test.widget1", $.ui.controller, {
		
			options: {
				template: 'widget1.html'
			},
				
			_init: function() {
				this._render();			
			}
		});
	})(jQuery);
});
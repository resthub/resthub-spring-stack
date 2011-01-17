define(['jquery.controller', 'jquery.ejs'], function() {
	(function($) {
		$.widget("test.widget1", $.ui.controller, {
		
			_init: function() {
				this._render();			
			}
		});
	})(jQuery);
});
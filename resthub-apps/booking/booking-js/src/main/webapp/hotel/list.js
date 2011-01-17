define(["models/hotel.model"], function() {
	(function($) {
	
	$.widget("booking.listHotels", $.ui.controller, {
		options: {
			searchVal: null,
			page: 0,
			size: 5,
			template: 'hotel/list.html'
		},
		_init: function() {
			Hotel.find($.proxy(this, '_displayHotels'),
						this.options.searchVal,
						this.options.page,
						this.options.size);
		},
		_displayHotels: function(result) {
			this._render({result: result});
	
			var self = this;
			$('#search-next').bind('click', function() {
				self.options.page++;
				self._init();
			});
		}
	});
	})(jQuery);
});
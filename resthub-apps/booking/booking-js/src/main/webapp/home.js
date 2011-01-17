define(['jquery.controller', 'booking/list', 'hotel/search'], function() {
(function($) {
	$.widget("booking.home", $.ui.controller, {
		options: {
			searchVal: null,
			size: 5,
			template: 'home.html'
		},
	
		_init: function() {
			this.options.cx.title('Home');	
			this.options.searchVal = $('#search-value').val();
			this.options.size = $('#search-size').val();
			this._render();
			
			$('#search').searchHotels({
				searchVal: this.options.searchVal,
				off: this.options.off,
				size: this.options.size,
				cx: this.options.cx
			});
			$('#booking-list').listBookings({cx: this.options.cx});
			this.options.cx.session('search-offset', 0);
		}
	});
	
})(jQuery);
});
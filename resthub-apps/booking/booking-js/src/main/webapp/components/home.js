define(['jquery.controller', 'components/booking/list', 'components/hotel/search'], function() {
(function($) {
	$.widget("booking.home", $.ui.controller, {
		options: {searchVal: null, size: 5},
	
		_init: function() {
			this.options.context.title('Home');	
			this.options.searchVal = $('#search-value').val();
			this.options.size = $('#search-size').val();
			this._render();
			
			$('#search').searchHotels({
				searchVal: this.options.searchVal,
				off: this.options.off,
				size: this.options.size,
				context: this.options.context
			});
			$('#booking-list').listBookings({context: this.options.context});
			this.options.context.session('search-offset', 0);
		}
	});
	
})(jQuery);
});
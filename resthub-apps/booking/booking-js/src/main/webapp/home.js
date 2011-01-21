define(['jquery','resthub.controller', 'booking/list', 'hotel/search', 'resthub.storage'], function($, Controller) {
	return $.widget("booking.home", $.ui.controller, {
		options: {
			searchVal: null,
			size: 5,
			template: 'home.html'
		},
	
		_init: function() {
			document.title = 'Home';	
			this.options.searchVal = $('#search-value').val();
			this.options.size = $('#search-size').val();
			this._render();
			
			$('#search').searchHotels({
				searchVal: this.options.searchVal,
				off: this.options.off,
				size: this.options.size
			});
			$('#booking-list').listBookings();
			$.storage.setItem('search-offset', 0);
		}
	});
});

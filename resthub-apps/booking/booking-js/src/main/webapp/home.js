define([ 'jquery', 'resthub.controller', 'booking/list', 'hotel/search', 'resthub.storage' ], function($, Controller) {

	return Controller.extend("HomeController", {
		searchVal : null,
		size : 5,
		template : 'home.html',

		init : function() {
			document.title = 'Home';
			this.searchVal = $('#search-value').val();
			this.size = $('#search-size').val();
			this.render();

			$('#search').search_hotels({
				searchVal : this.searchVal,
				size : this.size
			});
			$('#booking-list').list_bookings();
			$.storage.setItem('search-offset', 0);
		}
	});
});

define([ 'controller', 'booking/list', 'hotel/search' ], function(Controller) {

	return Controller.extend("HomeController", {
		searchVal : null,
		size : 5,
		template : 'home.html',

		init : function() {
			document.title = 'Home';
			this.render();

			$('#search').search_hotels({
				searchVal : this.searchVal,
				size : this.size
			});
			$('#booking-list').list_bookings();
			$.storage.set('search-offset', 0);
		}
	});
});

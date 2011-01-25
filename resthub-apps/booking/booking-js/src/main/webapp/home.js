define([ 'controller', 'booking/list', 'hotel/search' ], function(Controller) {

	return Controller.extend("HomeController", {
		template : 'home.html',

		init : function() {
			document.title = 'Home';
			this.render();

			$('#search').search_hotels();
			$('#booking-list').list_bookings();
		}
	});
});

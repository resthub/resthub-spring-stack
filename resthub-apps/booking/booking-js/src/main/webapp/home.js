define([ 'text!home.html', 'lib/controller', 'booking/list', 'hotel/search' ], function(tmpl, Controller) {

	return Controller.extend("HomeController", {
		template : tmpl,

		init : function() {
			document.title = 'Home';
			this.render();

			$('#search').search_hotels();
			$('#booking-list').list_bookings();
		}
	});
});

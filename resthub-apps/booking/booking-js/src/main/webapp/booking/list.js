define([ 'controller', 'repositories/booking.repository' ], function(Controller, BookingRepository) {

	return Controller.extend("ListBookingsController", {
		template : 'booking/list.html',
		
		init : function() {
			var user = $.storage.get('user');
			if (user.id) {
				BookingRepository.read($.proxy(this, '_displayBookings'), 'user/' + user.id);
			}
		},
		_displayBookings : function(bookings) {
			this.render({
				bookings : bookings
			});
		}
	});

});
define([ 'text!booking/list.html', 'lib/controller', 'repositories/booking.repository' ], function(tmpl, Controller, BookingRepository) {

	return Controller.extend("ListBookingsController", {
		template : tmpl,
		
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
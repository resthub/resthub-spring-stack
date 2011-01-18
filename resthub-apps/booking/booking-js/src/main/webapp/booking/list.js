define([ 'jquery', 'jquery.controller', 'models/booking.dao' ], function($, Controller, BookingDao) {
	$.widget("booking.listBookings", $.ui.controller, {
		options : {
			template : 'booking/list.html'
		},
		_init : function() {
			var user = this.options.cx.session('user');
			if (user.id) {
				Booking.read($.proxy(this, '_displayBookings'), 'user/'+ user.id);
			}
		},
		_displayBookings : function(bookings) {
			this._render({
				bookings : bookings
			});
		}
	});
});

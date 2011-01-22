define([ 'jquery', 'resthub.controller', 'repositories/booking.repository' ], function($, Controller, BookingRepository) {
	return $.widget("booking.deleteBooking", $.ui.controller, {
		options : {
			id : null
		},
		_init : function() {
			BookingRepository.remove($.proxy(this, '_bookingDeleted'), this.options.id);
		},
		_bookingDeleted : function() {
			$.publish('booking-deleted');
		}
	});
});

define([ 'controller', 'repositories/booking.repository' ], function(Controller, BookingRepository) {
	return Controller.extend("DeleteBookingController", {
		id : null,
		
		init : function() {
			BookingRepository.remove($.proxy(this, '_bookingDeleted'), this.options.id);
		},
		_bookingDeleted : function() {
			$.publish('booking-deleted');
		}
	});
});

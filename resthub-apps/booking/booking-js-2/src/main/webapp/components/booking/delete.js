define(['jquery.controller'], function() {
(function($) {

$.widget("booking.deleteBooking", $.ui.controller, {
    options: {
        id : null,
        context : null
    },
    _init: function() {
		this._delete('api/booking/' + this.options.id, this._bookingDeleted);
    },
	_bookingDeleted: function() {
		this.options.context.trigger('booking-deleted');
	}
});
})(jQuery);
});
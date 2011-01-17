define(['jquery.controller'], function() {
(function($) {

$.widget("booking.deleteBooking", $.ui.controller, {
    options: {
        id : null,
        cx : null
    },
    _init: function() {
		this._delete('api/booking/' + this.options.id, this._bookingDeleted);
    },
	_bookingDeleted: function() {
		this.options.cx.trigger('booking-deleted');
	}
});
})(jQuery);
});
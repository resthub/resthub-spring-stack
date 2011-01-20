define([ 'jquery.controller', 'dao/booking.dao' ], function() {
	(function($) {

		$.widget("booking.listBookings", $.ui.controller, {
			options : {
				template : 'booking/list.html'
			},
			_init : function() {
				var user = $.storage.getJSONItem('user');
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
	})(jQuery);
});
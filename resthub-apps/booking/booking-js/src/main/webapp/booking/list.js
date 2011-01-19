define([ 'jquery', 'resthub.controller', 'dao/booking.dao' ], function($, Controller) {
	(function($) {
	$.widget("booking.listBookings", $.ui.controller, {
		options : {
			template : 'booking/list.html'
		},
		_init : function() {
			var user $.storage.getJSONItem('user');
			if (user.id) {
				BookingDao.read($.proxy(this, '_displayBookings'), 'user/'+ user.id);
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

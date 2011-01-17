(function($) {

$.widget("booking.listBookings", $.ui.controller, {
	options: {
		template : 'booking/list.html',
		cx : null
	},
	_init: function() {
		var user = this.options.cx.session('user');
		var url;
		if(user.id) {
			url = 'api/booking/user/' + user.id;
			this._get(url, this._displayBookings);
		}
	},
	_displayBookings: function(bookings) {
		this.element.render(this.options.template, {bookings: bookings});
	}
});
})(jQuery);
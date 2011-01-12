(function($) {

var listBookings =
{
	options: {
		template : 'components/booking/list.html',
		context : null
	},
	_init: function() {
		var user = this.options.context.session('user');
		var url;
		if(user.id) {
			url = 'api/booking/user/' + user.id;
			this._get(url, this._displayBookings);
		}
	},
	_displayBookings: function(bookings) {
		this.element.render(this.options.template, {bookings: bookings});
	}
};

$.widget("booking.listBookings", $.resthub.resthubController, listBookings);
})(jQuery);
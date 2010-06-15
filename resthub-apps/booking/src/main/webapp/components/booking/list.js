(function($) {

var listBookings =
{
	options: {
		user : null,
		template : 'components/booking/list.html',
		context : null
	},
	_create: function() {
		this.element.addClass('bd-booking-list');
	},
	_init: function() {
		this.options.context.title('Home');
		var url;
		if(this.options.user.id) {
			url = 'api/booking/user/' + this.options.user.id;
			this._get(url, this, '_displayBookings');
		}
	},
	_displayBookings: function(bookings) {
		
		this.element.render(this.options.template, {bookings: bookings});
		this.options.context.session('search-offset', 0);

		var self = this;
		$('#search-submit').bind('click', function() {
			self.options.context.session('search-offset', 0);
			self.options.context.trigger('hotel-search');
		});

		$('#search-value').bind('keyup', function() {
			self.options.context.session('search-offset', 0);
			self.options.context.trigger('hotel-search');
		});

		$('#search-limit').bind('change', function() {
			self.options.context.session('search-offset', 0);
			self.options.context.trigger('hotel-search');
		});
	},
	destroy: function() {
		this.element.removeClass('bd-booking-list');
		$.Widget.prototype.destroy.call( this );
	}
};

$.widget("booking.listBookings", $.resthub.resthubController, listBookings);
})(jQuery);
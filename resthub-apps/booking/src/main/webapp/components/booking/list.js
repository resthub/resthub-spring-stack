(function($) {

var listBookings =
{
	options: {
		data : {},
		template : 'components/booking/list.html',
		context : null
	},
	_create: function() {
		this.element.addClass('bd-booking-list');
	},
	_init: function() {
		var url;
		if(this.options.data.user.id) {
			url = 'api/booking/user/' + this.options.data.user.id;
		} else {
			url = 'api/booking';
		}
		this._ajax(url, this, '_displayBookings');
	},
	_displayBookings: function(bookings) {
		
		this.element.render(this.options.template, {bookings: bookings});

		this.element.find('tr.booking-item').click(function() {
			var id = $(this).attr('id').split("-")[1];
			self.options.context.redirect('#/booking', id);
		});

		var self = this;
		$('#search-submit').bind('click', function() {
			var searchVal = $('#search-value').val();
			self.options.context.redirect('#/hotel/search?q=' + searchVal);
		});
	},
	destroy: function() {
		this.element.removeClass('bd-booking-list');
		$.Widget.prototype.destroy.call( this );
	}
};

$.widget("booking.listBookings", $.resthub.resthubController, listBookings);
})(jQuery);
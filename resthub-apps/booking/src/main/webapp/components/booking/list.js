$.widget("booking.listBookings", {
	options: {
		data : {},
		template : 'components/booking/list.html',
		context : null
	},
	_create: function() {
		this.element.addClass('bd-booking-list');
	},
	_init: function() {
		var self = this;
		this.element.render(this.options.template, {bookings: this.options.data});

		this.element.find('tr.booking-item').click(function() {
			var id = $(this).attr('id').split("-")[1];
			self.options.context.redirect('#/booking', id);
		});

		$('#search-submit').bind('click', function() {
			var searchVal = $('#search-value').val();
			self.options.context.redirect('#/hotel/search?q=' + searchVal);
		});
	},
	destroy: function() {
		this.element.removeClass('bd-booking-list');
		$.Widget.prototype.destroy.call( this );
	}
});


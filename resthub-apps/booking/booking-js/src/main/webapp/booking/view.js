define([ 'jquery', 'repositories/booking.repository', 'resthub.controller', 'jquery.json',  ], function($, Booking ) {
	$.widget("booking.viewBooking", $.ui.controller, {
		options : {
			booking : {},
			template : 'booking/view.html',
			mode : 'view'
		},
		_init : function() {

			if (this.options.mode == 'view') {
				BookingRepository.read($.proxy(this, '_displayViewMode'), this.options.booking.id);
			} else {
				this._displayConfirmMode();
			}
		},
		_displayViewMode : function() {
			this._render({
				booking : this.options.booking,
				mode : this.options.mode
			});
		},
		_displayConfirmMode : function() {
			this.options.booking = $.storage.getJSONItem('booking');
			var daysBetween = $.storage.getItem('daysBetween');
			var total = daysBetween * this.options.booking.hotel.price;

			this._render({
				booking : this.options.booking,
				total : total,
				mode : this.options.mode
			});

			$('#content h1:first').html("Confirm hotel booking");

			$('input#book-revise').unbind();
			$('input#book-revise').bind('click', $.proxy(this, '_reviseBooking'));

			$('input#book-confirm').unbind();
			$('input#book-confirm').bind('click', $.proxy(this, '_sendBooking'));
		},
		_reviseBooking : function() {
			$.route('#/booking/hotel/' + this.options.booking.hotel.id);
		},
		_sendBooking : function() {
			BookingRepository.save($.proxy(this, '_endOfBooking'), $.toJSON(this.options.booking));
		},
		/* Go back home page and trigger end-of-booking event */
		_endOfBooking : function(booking) {
			$.storage.setJSONItem('booking', booking);
			$.route('#/home');
			$.publish('end-of-booking');
		}
	});

});

define(['text!booking/view.html', 'lib/controller', 'repositories/booking.repository'], function(tmpl, Controller, BookingRepository ) {
	Controller.extend("ViewBookingController", {
		booking : {},
		template : tmpl,
		mode : 'view',
		
		init : function() {

			if (this.mode == 'view') {
				BookingRepository.read($.proxy(this, '_displayViewMode'), this.booking.id);
			} else {
				this._displayConfirmMode();
			}
		},
		_displayViewMode : function() {
			this.render({
				booking : this.booking,
				mode : this.mode
			});
		},
		_displayConfirmMode : function() {
			this.booking = $.storage.get('booking');
			var daysBetween = $.storage.get('daysBetween');
			var total = daysBetween * this.booking.hotel.price;

			this.render({
				booking : this.booking,
				total : total,
				mode : this.mode
			});

			$('#content h1:first').html("Confirm hotel booking");

			$('input#book-revise').unbind();
			$('input#book-revise').bind('click', $.proxy(this, '_reviseBooking'));

			$('input#book-confirm').unbind();
			$('input#book-confirm').bind('click', $.proxy(this, '_sendBooking'));
		},
		_reviseBooking : function() {
			$.route('#/booking/hotel/' + this.booking.hotel.id);
		},
		_sendBooking : function() {
			BookingRepository.save($.proxy(this, '_endOfBooking'), $.toJSON(this.booking));
		},
		/* Go back home page and trigger end-of-booking event */
		_endOfBooking : function(booking) {
			$.storage.set('booking', booking);
			$.route('#/home');
			$.publish('end-of-booking');
		}
	});

});

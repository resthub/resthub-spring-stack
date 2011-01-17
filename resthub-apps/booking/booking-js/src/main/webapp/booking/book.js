define(["booking/view", "booking/edit"], function() {
(function($) {
	$.widget("booking.bookBooking", $.ui.controller, {
	options: {
		hotelId: null,
		booking: {},
		template: 'booking/book.html',
		cx: null,
		mode: 'edit'
	},
	_init: function() {

		this.element.render(this.options.template, {hotelId: this.options.hotelId});
		
		this.options.booking = this.options.cx.session('booking');
		
		if(this.options.booking == undefined) {
			this._get('api/hotel/' + this.options.hotelId, this._initBookingData);
		} else {
			this._displayBookingView(this.options.booking);
		}
	},
	/**
	 * If there is no booking in session when this widget starts, this function
	 * creates a booking with user and hotel...
	 */
	_initBookingData: function(hotel) {
		this.options.booking = {
			user: this.options.cx.session('user'),
			hotel: hotel
		};
		this.options.cx.session('booking', this.options.booking);
		this._displayBookingView(this.options.booking);
	},
	_displayBookingView: function() {

		var self = this;
		
		$('#hotel-data').viewHotel({
			id: self.options.booking.hotel.id,
			only_data: true
		});
	
		
		if(this.options.mode == 'edit') {
			this._switchToEdit();
		} else {
			this._switchToView();
		}
	},
	_switchToEdit: function() {

		Sammy.log('Booking workflow : edit mode.');
		
		$('#booking-data').editBooking({
			booking: this.options.booking,
			cx: this.options.cx
		});
		
	},
	_switchToView: function() {
		var self = this;
		Sammy.log('Booking workflow : confirmation mode.');
		$('#booking-data').viewBooking({
			booking: self.options.booking,
			cx: self.options.cx,
			mode: 'confirm'
		});
	}
});
})(jQuery);
});
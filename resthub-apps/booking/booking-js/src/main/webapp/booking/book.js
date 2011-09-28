define([ 'text!booking/book.html', 'lib/controller', 'repositories/hotel.repository', 'booking/view', 'booking/edit' ], function(tmpl, Controller, HotelRepository) {
	Controller.extend("BookBookingController", {
		
		hotelId : null,
		booking : {},
		template : tmpl,
		mode : 'edit',
		
		init : function() {
			this.render({
				hotelId : this.hotelId
			});
			this.booking = $.storage.get('booking');

			if (this.booking == undefined) {
				HotelRepository.read($.proxy(this, '_initBookingData'), this.hotelId);
			} else {
				this._displayBookingView(this.booking);
			}
		},
		/**
		 * If there is no booking in session when this widget starts, this
		 * function creates a booking with user and hotel...
		 */
		_initBookingData : function(hotel) {
			this.booking = {
				user : $.storage.get('user'),
				hotel : hotel
			};
			$.storage.set('booking', this.booking);
			this._displayBookingView(this.booking);
		},
		_displayBookingView : function() {

			var self = this;

			$('#hotel-data').view_hotel({
				id : self.booking.hotel.id,
				only_data : true
			});

			if (this.mode == 'edit') {
				this._switchToEdit();
			} else {
				this._switchToView();
			}
		},
		_switchToEdit : function() {
			var self = this;
			console.log('Booking workflow : edit mode.');

			$('#booking-data').edit_booking({
				booking : self.booking
			});

		},
		_switchToView : function() {
			var self = this;
			console.log('Booking workflow : confirmation mode.');
			$('#booking-data').view_booking({
				booking : self.booking,
				mode : 'confirm'
			});
		}
	});
});

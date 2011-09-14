define([ 'text!hotel/view.html', 'lib/controller', 'repositories/hotel.repository' ], function(tmpl, Controller, HotelRepository) {
	Controller.extend("ViewHotelController", {
		id : null,
		template : tmpl,
		only_data : false,
		
		init : function() {
			if (!isNaN(this.id)) {
				HotelRepository.read($.proxy(this, '_displayHotel'), this.id);
			}
		},
		_displayHotel : function(hotel) {
			this.render({
				hotel : hotel,
				only_data : this.only_data
			});

			var id = hotel.id;
			$('input#book-request').bind('click', function() {
				var booking = {
					hotel : hotel,
					user : $.storage.get('user')
				};
				$.storage.set('booking', booking);
				$.route('#/booking/hotel/' + id);
			});
		}
	});
});

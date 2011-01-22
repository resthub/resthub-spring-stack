define([ 'resthub.controller', 'repositories/hotel.repository' ],
		function(Controller, HotelRepository) {
			$.widget("booking.viewHotel", $.ui.controller, {
				options : {
					id : null,
					template : 'hotel/view.html',
					only_data : false
				},
				_init : function() {
					if (!isNaN(this.options.id)) {
						HotelRepository.read($.proxy(this, '_displayHotel'),
								this.options.id);
					}
				},
				_displayHotel : function(hotel) {
					this._render({
						hotel : hotel,
						only_data : this.options.only_data
					});

					var id = hotel.id;
					$('input#book-request').bind('click', function() {
						var booking = {
							hotel : hotel,
							user : $.storage.getJSONItem('user')
						};
						$.storage.setJSONItem('booking', booking);
						$.route('#/booking/hotel/'+ id);
					});
				}
			});
		});

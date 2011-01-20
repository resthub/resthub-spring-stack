define([ 'resthub.controller', 'dao/hotel.dao' ],
		function(Controller, HotelDao) {
			$.widget("booking.viewHotel", $.ui.controller, {
				options : {
					id : null,
					template : 'hotel/view.html',
					only_data : false
				},
				_init : function() {
					if (!isNaN(this.options.id)) {
						HotelDao.read($.proxy(this, '_displayHotel'),
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
						location.hash = '#/booking/hotel/'+ id;
					});
				}
			});
		});

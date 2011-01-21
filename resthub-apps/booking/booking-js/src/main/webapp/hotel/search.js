define([ "jquery", "hotel/list" ], function($) {
		$.widget("booking.searchHotels", $.ui.controller, {
			options : {
				searchVal : null,
				size : 5,
				template : 'hotel/search.html',
				delay : 1000,
				searching : null
			},
			_init : function() {

				this._render();
				var self = this;

				$('#search-submit').bind('click', function() {
					$.storage.setItem('search-offset', 0);
					$.publish('hotel-search');
				});
					
				$('#search-value').bind('keyup', function() {
					clearTimeout(self.options.searching);
					self.options.searching = setTimeout(function() {
						$.storage.setItem('search-offset', 0);
						$.publish('hotel-search');
					}, self.options.delay);
				});

				$('#search-size').bind('change', function() {
					$.storage.setItem('search-offset', 0);
					$.publish('hotel-search');
				});

				$('#search-size option[value=' + this.options.size + ']').attr('selected', 'selected');
				$('#search-value').attr('value', this.options.searchVal);

				if (this.options.searchVal != '#home') {
					$('#result').listHotels({
						searchVal : self.options.searchVal,
						size : self.options.size
					});
				}
			}
		});
});

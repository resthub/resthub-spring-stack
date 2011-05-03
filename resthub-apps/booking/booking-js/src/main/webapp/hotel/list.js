define([ 'lib/controller', 'repositories/hotel.repository' ], function(Controller, HotelRepository) {
	return Controller.extend("ListHotelsController", {
		template : 'hotel/list.html',
		lastSearchedVal: '',
		hotelSearchHandle: {},
		
		init : function() {
			this.hotelSearchHandle = $.subscribe('hotel-search', $.proxy(this, '_findHotels'));
			$.publish('hotel-search', '');
		},
		destroy: function() {
			$.unsubscribe(this.hotelSearchHandle);
	        this._super();
		},
		_findHotels: function(value) {
			this.lastSearchedVal = value;
			HotelRepository.find($.proxy(this, '_displayHotels'),
								 value,
								 $.storage.get('search-page'),
								 $.storage.get('search-size'));
		},
		_displayHotels : function(result) {
			this.render({
				result : result
			});
			$('#search-next').bind('click', $.proxy(this, '_nextPage'));
		},
		_nextPage : function() {
			var page = $.storage.get('search-page');
			var page = $.storage.set('search-page', ++page);
			$.publish('hotel-search', this.lastSearchedVal);
		}
	});
});

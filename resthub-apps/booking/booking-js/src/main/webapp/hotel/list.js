define([ 'controller', 'repositories/hotel.repository' ], function(Controller, HotelRepository) {
	return Controller.extend("ListHotelsController", {
		searchVal : null,
		page : 0,
		size : 5,
		template : 'hotel/list.html',
		
		init : function() {
			HotelRepository.find($.proxy(this, '_displayHotels'), this.searchVal, this.page, this.size);
		},
		_displayHotels : function(result) {
			this.render({
				result : result
			});
			var self = this;
			$('#search-next').bind('click', $.proxy(this, '_nextPage'));
		},
		_nextPage : function() {
			this.page++;
			this.init();
		}
	});
});

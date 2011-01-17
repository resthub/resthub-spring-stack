(function($) {

$.widget("booking.listHotels", $.ui.controller, {
	options: {
		searchVal: null,
		page: 0,
		size: 5,
		template: 'hotel/list.html'
	},
	_init: function() {
		var url;
		if(this.options.searchVal) {
			url = 'api/hotel/search?q=' + this.options.searchVal + '&page=' + this.options.page + '&size=' + this.options.size;
		} else {
			url = 'api/hotel/search?page=' + this.options.page + '&size=' + this.options.size;
		}
		this._get(url, this._displayHotels);
	},
	_displayHotels: function(result) {
		this._render({result: result});

		var self = this;
		$('#search-next').bind('click', function() {
			self.options.page++;
			self._init();
		});
	}
});
})(jQuery);
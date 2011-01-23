define([ 'controller', 'hotel/list' ], function(Controller) {
	Controller.extend("SearchHotelsController", {
		
		searchVal : null,
		size : 5,
		template : 'hotel/search.html',
		delay : 1000,
		searching : null,

		init : function() {

			this.render();
			var self = this;

			$('#search-submit').bind('click', function() {
				$.storage.set('search-offset', 0);
				$.publish('hotel-search');
			});

			$('#search-value').bind('keyup', function() {
				clearTimeout(self.searching);
				self.searching = setTimeout(function() {
					$.storage.set('search-offset', 0);
					$.publish('hotel-search');
				}, self.delay);
			});

			$('#search-size').bind('change', function() {
				$.storage.set('search-offset', 0);
				$.publish('hotel-search');
			});

			$('#search-size option[value=' + this.size + ']').attr('selected', 'selected');
			$('#search-value').attr('value', this.searchVal);

			if (this.searchVal != '#home') {
				$('#result').list_hotels({
					searchVal : self.searchVal,
					size : self.size
				});
			}
		}
	});
});

define([ "jquery", "resthub.controller", "hotel/list" ], function($, Controller) {
	Controller.extend("SearchHotelsController", {
			options : {
				searchVal : null,
				size : 5,
				template : 'hotel/search.html',
				delay : 1000,
				searching : null
			},
			init : function() {

				this.render();
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
					$('#result').list_hotels({
						searchVal : self.options.searchVal,
						size : self.options.size
					});
				}
			}
		});
});

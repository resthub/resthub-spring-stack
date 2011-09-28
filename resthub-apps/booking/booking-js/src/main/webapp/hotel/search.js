define([ 'text!hotel/search.html', 'lib/controller', 'hotel/list' ], function(tmpl, Controller) {
	Controller.extend("SearchHotelsController", {
		
		template : tmpl,
		delay : 1000,
		searching : null,

		init : function() {

			this.render();
			var self = this;
			

			$('#search-submit').bind('click.SearchHotelsController', function() {
				var value = $('#search-value').val();
				$.storage.set('search-page', 0);
				$.publish('hotel-search', value);
			});

			$('#search-value').bind('keyup.SearchHotelsController', function() {
				clearTimeout(self.searching);
				self.searching = setTimeout(function() {
					var value = $('#search-value').val();
					$.storage.set('search-page', 0);
					$.publish('hotel-search', value);
				}, self.delay);
			});

			$('#search-size').bind('change.SearchHotelsController', function() {
				var value = $('#search-value').val();
				$.storage.set('search-page', 0);
				$.storage.set('search-size', $('#search-size').val());
				$.publish('hotel-search', value);
			});

			$('#search-size option[value=' + $('#search-size').val() + ']').attr('selected', 'selected');

			$('#result').list_hotels();

		},
                
                destroy: function() {
                   $('#search-submit').unbind('.SearchHotelsController');
                   $('#search-value').unbind('.SearchHotelsController');
                   $('#search-size').unbind('.SearchHotelsController');
                   this._super();
                }
	});
});

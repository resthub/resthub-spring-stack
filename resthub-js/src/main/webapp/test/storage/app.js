define(['storage'], function() {

	$(document).ready(function(){
		
		$('.setitem1').click(function() {
			$.storage.set('item1', 'lorem ipsum');
			$('#main').text('item1 stored');
		});
		
		$('.getitem1').click(function() {
			var item = $.storage.get('item1');
			if(!item) {
				item = 'null';
			}
			$('#main').text(item);
		});
		
		$('.setitem2').click(function() {
			$.storage.set('item2', {lorem: 'ipsum'});
			$('#main').text('item2 stored');
		});
		
		$('.getitem2').click(function() {
			var item = $.storage.get('item2');
			if(!item) {
				item = {lorem: 'null'};
			}
			$('#main').text(item.lorem);
		});
		
		$('.removeitem1').click(function() {
			$.storage.remove('item1');
			$('#main').text('item1 removed');
		});
		
		$('.removeitem2').click(function() {
			$.storage.remove('item2');
			$('#main').text('item2 removed');
		});
		
		$('.clear').click(function() {
			$.storage.clear();
			$('#main').text('All items cleared');
		});
		
	});
});
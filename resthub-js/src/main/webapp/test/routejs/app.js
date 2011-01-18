define(['jquery', 'route'], function() {

	$(document).ready(function(){
		route('#').bind(function(){
			$('#main').html('<p>Home</p>');
		});
		route('#/route1').bind(function(){
			$('#main').html('<p>Route 1</p>');
		});
		route('#/route2').bind(function(){
			$('#main').html('<p>Route 2</p>');
		});
	});
});
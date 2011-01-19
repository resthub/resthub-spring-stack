define(['jquery', 'route', 'console'], function() {

	$(document).ready(function(){
		route('#').bind(function(){
			$('#main').html('<p>Home</p>');
			console.info('home');
		});
		route('#/route1').bind(function(){
			$('#main').html('<p>Route 1</p>');
			console.warn('route 1');
		});
		route('#/route2').bind(function(){
			$('#main').html('<p>Route 2</p>');
			console.log('route 2');
		});
		route('#/route3/:id').bind(function(params){
			$('#main').html('<p>Route 3, id = </p>' + params.id);
			console.log('route 3, id = ' + params.id);
		});
	});
});
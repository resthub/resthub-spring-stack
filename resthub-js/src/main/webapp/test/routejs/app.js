define(["route"], function() {
	
	$(document).ready(function(){
		
		// Define routes
		$.route('#', function() {
			$('#main').html('<span>Root</span>');
		});
				
		$.route('#/login', function() {
			$('#main').html('<span>Hello you</span>');
		});
		
		// Test multiple callbacks on the same route
		$.route('#/login', function() {
			alert('Hello you');
		});
		
		$.route('#/logout', function() {
			$('#main').html('<span>See ya !</span>');
		});
		
		$.route('#/link/:id', function(p) {
			$('#main').html('Link ' + p.id);
		});
		
		$.route('#/toto/:id/tutu/:id2', function(p) {
			$('#main').html('Toto ' + p.id + ', tutu ' + p.id2);
		});
		
		// Listen to the run-route event on the event bus
		$.subscribe('route-run', function(path) {
			console.debug("route-run event for route " + path);
		});
		
		// Run current route
		$.route(location.hash);
				
	});

});

define(["jquery", "resthub.route"], function($) {
	
	$(document).ready(function(){
		
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
		
		$.route(location.hash);
				
	});

});

define(["sammy"], function() {

	var app = $.sammy(function() {
		this.get('#/', function(context) {
			$('#main').html("Home");
		});
		
		this.get('#/route1', function(context) {
			$('#main').html("Route 1");
		});
		
		this.get('#/route2', function(context) {
			$('#main').html("Route 2");
		});
	});

	$(function() {
		app.run('#/');
	});

});
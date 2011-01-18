define(["jquery","sammy"], function($) {

	var app = $.sammy(function() {
		this.get('#/', function(context) {
			$('#main').html("Home");
		});
		
		this.get('#/controller1', function(context) {
			require(["test/controller/widget1"], function() {
				$('#main').widget1(context);
			});
		});

	});

	$(function() {
		app.run('#/');
	});

});

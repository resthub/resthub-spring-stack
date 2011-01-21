define(["jquery","jquery.tinypubsub", "route"], function($) {

	$(document).ready(function(){
		route('#').bind( function() {
			$('#main').html("Home");
		});
		
		route('#/controller1').bind( function() {
			require(["test/controller/widget1"], function() {
				$('#main').widget1();
			});
		});

		route('#').run();
	});
});

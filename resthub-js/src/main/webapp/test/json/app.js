define(["sammy", "jquery.json"], function() {

	var app = $.sammy(function() {
		this.get('#/', function(context) {
			$('#main').html("Home");
		});
		
		this.get('#/tojson', function(context) {
			var thing = {plugin: 'jquery-json', version: 2.2};
			var encoded = $.toJSON(thing);
			$('#main').html(encoded);
		});
		
		this.get('#/evaljson', function(context) {
			var thing = {plugin: 'jquery-json', version: 2.2};
			var encoded = $.toJSON(thing);
			var name = $.evalJSON(encoded).plugin;
			var version = $.evalJSON(encoded).version;
			$('#main').html("Name : " + name + ", Version : " + version);
		});
	});

	$(function() {
		app.run('#/');
	});

});
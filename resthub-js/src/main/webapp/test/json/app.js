define(['jquery.json'], function() {

	$('.tojson').click(function() {
		var thing = {plugin: 'jquery-json', version: 2.2};
		var encoded = $.toJSON(thing);
		$('#main').html(encoded);
	});
	
	$('.evaljson').click(function() {
		var thing = {plugin: 'jquery-json', version: 2.2};
		var encoded = $.toJSON(thing);
		var name = $.evalJSON(encoded).plugin;
		var version = $.evalJSON(encoded).version;
		$('#main').html("Name : " + name + ", Version : " + version);
	});
	
});
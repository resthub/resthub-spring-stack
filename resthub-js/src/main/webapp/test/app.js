define(["sammy", "sammy.storage", "sammy.title", "sammy.json", "jquery.ejs", "jquery.controller"], function() {

	var app = $.sammy(function() {
		this.get('#/', function(context) {
			context.log('Test page initialization !');
		});
		
		this.get('#/alert', function(context) {
			alert('This is a test !');
		});
	});

	$(function() {
		app.run('#/');
	});

});
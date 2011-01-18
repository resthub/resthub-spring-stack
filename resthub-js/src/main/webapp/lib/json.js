define(['jquery', 'jquery.json', 'sammy'], function($) {
	var JSON = {stringify: $.toJSON, parse: $.secureEvalJSON};
	Sammy.JSON = function() { return JSON };
	return JSON;
})

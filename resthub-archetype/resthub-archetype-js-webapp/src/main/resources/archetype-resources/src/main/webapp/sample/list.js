define([ 'text!sample/list.html', 'lib/controller', 'repositories/sample.repository' ], function(tmpl, Controller, SampleRepository) {
	return Controller.extend("ListSamplesController", {
		template : tmpl,
		
		init : function() {
			SampleRepository.listAll($.proxy(this, '_displaySamples'));
		},
		_displaySamples: function(samples) {
			this.render({samples : samples});
		}
	});
});

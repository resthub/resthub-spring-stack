define([ 'lib/controller', 'repositories/sample.repository' ], function(Controller, SampleRepository) {
	return Controller.extend("ListSamplesController", {
		template : 'sample/list.html',
		
		init : function() {
			SampleRepository.listAll($.proxy(this, '_displaySamples'));
		},
		_displaySamples: function(samples) {
			this.render({samples : samples});
		}
	});
});

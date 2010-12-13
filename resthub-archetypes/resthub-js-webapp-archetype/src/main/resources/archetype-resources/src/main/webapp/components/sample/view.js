(function($) {

	var viewSample =
	{
		options: {
			template: 'components/sample/view.html',
			context: null,
			sampleId: null
		},
		_init: function() {
			this._get('api/sample/' + this.options.sampleId, this._displayElement);
		},
		_displayElement: function(result) {
			this.element.render(this.options.template, {sample: result});
		}
	};

	$.widget("sampleApp.viewSample", $.resthub.resthubController, viewSample);

})(jQuery);
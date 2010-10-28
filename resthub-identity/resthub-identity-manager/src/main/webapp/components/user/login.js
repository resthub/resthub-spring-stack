(function($) {

	var loginUsers =
	{
		options: {
			template: 'components/user/login.html',
			context: null,
			sampleId: null
		},
		_init: function() {	
			var self=this;	
			self.element.render(self.options.template);
				}
			}; 

	$.widget("user.loginUsers", $.resthub.resthubController, loginUsers);

})(jQuery);
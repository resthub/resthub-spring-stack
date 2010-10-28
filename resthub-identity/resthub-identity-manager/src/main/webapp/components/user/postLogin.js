(function($) {

	var postLoginUsers =
	{
		options: {
			template: 'components/user/Home.html',
			context: null,
			sampleId: null,
			
			userLogin: null,
			userPassword: null,
			redirectURL:null
		},
		_init: function() {	
			var self=this;
			
			var info={};
			info.login= self.options.userLogin;
			info.password= self.options.userPassword;
			this._post('api/user/login2/', this._displayHome, $.toJSON(info) );
				}
		,
		_displayHome: function(user) {
				this.element.render(this.options.template, {
					user: user
				});
			}
	}
	$.widget("user.postLoginUsers", $.resthub.resthubController, postLoginUsers);

})(jQuery);
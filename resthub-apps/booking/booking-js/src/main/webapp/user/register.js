define([ 'jquery.controller', 'jquery.json', 'models/user.model' ], function() {
	(function($) {
		$.widget("booking.userRegister", $.ui.controller, {
			options : {
				template : 'user/register.html'
			},
			_init : function() {

				this.cx().title('Register');
				this._render();

				var self = this;
				$('#register-button').bind('click', function() {
					var user = {
						email : $('input[name=email]').val(),
						username : $('input[name=username]').val(),
						password : $('input[name=password]').val(),
						fullname : $('input[name=name]').val()
					};
					User.save($.proxy(self, '_userRegistered'), $.toJSON(user));
				});
			},
			_userRegistered : function(user) {
				this.cx().trigger('user-registered', user);
			}
		});
	})(jQuery);
});
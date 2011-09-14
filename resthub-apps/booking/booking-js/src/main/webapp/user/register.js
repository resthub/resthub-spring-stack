define([ 'text!user/register.html', 'lib/controller', 'repositories/user.repository' ], function(tmpl, Controller, UserRepository) {
		Controller.extend("UserRegisterController", {
			template : tmpl,
			init : function() {

				document.title = 'Register';
				this.render();

				var self = this;
				$('#register-button').bind('click', function() {
					var user = {
						email : $('input[name=email]').val(),
						username : $('input[name=username]').val(),
						password : $('input[name=password]').val(),
						fullname : $('input[name=name]').val()
					};
					UserRepository.save($.proxy(self, '_userRegistered'), $.toJSON(user));
				});
			},
			_userRegistered : function(user) {
				$.publish('user-registered', user);
			}
		});
});

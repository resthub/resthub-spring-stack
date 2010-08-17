(function($) {

var editUser =
{
	options: {
		template: 'components/user/edit.html',
		context: null,
		groups: null
	},
	_init: function() {
		this._prepareData();
	},
	_prepareData: function() {
		this._get( 'api/group/all' , this._setGroups );
	},
	_setGroups: function(groups) {
		this.options.groups = groups;
		this._displayUserForm();
	},
	_displayUserForm: function() { 

		var user = this.options.context.session('tempUser');

		this.element.render( this.options.template, {
					user: user,
					groups: this.options.groups
				});

		$('#content h1:first').html("Create user");

		this._sessionToForm();
		
		$('form#user-form').validate({errorElement: 'span'});

		$('input#user-proceed').unbind();
		$('input#user-proceed').bind('click', $.proxy(this._sendUserData, this));
	},
	/* Tests if the form is filled correctly and sends the data */
	_sendUserData: function() {
		var validForm = $('form#user-form').validate({errorElement: 'span'}).form();
		if (validForm) {
			this._formToSession();
			var user = this.options.context.session('tempUser');
			this._post( 'api/user', this._endOfProcess, $.toJSON(user));
		}
	},
	/* Puts form data in session */
	_formToSession: function() {
		var user = {
			groups: [],
			permissions: []
		};
		
		user.firstName = $('input[name=firstName]').val();
		user.lastName = $('input[name=lastName]').val();
		user.login = $('input[name=login]').val();
		user.password = $('input[name=password]').val();
		user.email = $('input[name=email]').val();
		$('input[name=usergroup]:checked').each(function(index, element) {
			user.groups.push({
				id: $(element).attr('value')
			});
		});
		
		this.options.context.session('tempUser', user);
	},
	/* Displays session data in user form */
	_sessionToForm: function() {
		var user = this.options.context.session('tempUser');
		if(user != null) {
			$('input[name=firstName]').val(user.firstName);
			$('input[name=lastName]').val(user.lastName);
			$('input[name=login]').val(user.login);
			$('input[name=password]').val(user.password);
			$('input[name=email]').val(user.email);
			for( var index in user.groups ) {
				$('input[value=' + user.groups[index].id + ']').attr('checked', true);
			}
		}
	},
	_endOfProcess: function(user) {
		console.log(user);
		// Cleans the tempUser in session
		this.options.context.session('tempUser', null);
		this.options.context.redirect('#/user/details/' + user.login);
	}
};

$.widget("booking.editUser", $.resthub.resthubController, editUser);
})(jQuery);
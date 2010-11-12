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
		if (this.options.context.session('accessToken') != null) {
			this._securedGet('api/group/list', this._setGroups);
		}
		else {
			this._displayUserForm();
		}
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
		
		/* We want the password to be strong enough to we check is strenght*/
		$('input#password').unbind();
		$('input#password').bind('keyup', this._EvalPwd );
		
		/* We want the user to be sure about his password (which is hidden), so we ask it a second time*/
		$('input#passwordCheck').unbind();
		$('input#passwordCheck').bind('keyup', this._EvalPwdAreEgals );
	},
	
	_EvalPwd : function(){
		editUser._EvalPwdStrength();
		/* The audit of the similarity of the passwords is done when we a password fields*/
		editUser._EvalPwdAreEgals();
	},
	
	/* check if text inside the passwords input field are egals or not*/
	/* @return boolean */
	_arePwdEgals : function(){
		var pwd1= $('input[name=password]').val();
		var pwd2= $('input[name=passwordCheck]').val();
		
		return (pwd1==pwd2) ? true : false;
	},
	
	
	
	/* change text and class {good,bad} of the passwordsAreEgals span*/
	_EvalPwdAreEgals : function(){
		
		var fieldText;
			$('span#passwordsAreEgals').removeClass('good bad');
			
		if(editUser._arePwdEgals()){
			$('span#passwordsAreEgals').addClass('good');
			fieldText="PasswordsEgals";
		}else {
			$('span#passwordsAreEgals').addClass('bad');
			fieldText="PasswordsNotEgals";
		}
		$('span#passwordsAreEgals').text(l(fieldText));				
	},	
	
	/* Evaluate strength of the text in the password input field as a password*/
	/* change text {Secured,Not Well Secured,NotSecured} and  class of the passwordStrength span*/
	_EvalPwdStrength : function(){
	const  NOT_SECURED_LEVEL = 12; 
	const SECURED_LEVEL = 20;
		var text=$('input[name=password]').val();
		var strength=0;
		var capitalLetter=0;
		var smallLetter=0;
		var digixCharactere=0;
		var poncuationCharactere=0;
		var characteresType=new Array();
		var c=0;
		
		if(text.length>5) {
			strength+=Math.min(10,text.length);
		}
		for(characterIndex=0;
				characterIndex<text.length;
				characterIndex=characterIndex+1){
			charactere=text.charAt(characterIndex);
			if((charactere >= 'A') && (charactere <= 'Z')){
				capitalLetter=capitalLetter+1;
				continue;
			}	
	
			if((charactere >= 'a') && (charactere <= 'z')){
				smallLetter=smallLetter+1;
				continue;
			}	
			
			if ((charactere >= '0') && (charactere <= '9')){
				digixCharactere=digixCharactere+1;
				continue;
			}	
			
			if ("!@#$%^&*()_+-='\";:[{]}\|.>,</?`~".indexOf(charactere) >= 0){
				poncuationCharactere=1+ poncuationCharactere;
				continue;
			}
		}
		
		characteresType[0]=capitalLetter;
		characteresType[1]=smallLetter;
		characteresType[2]=digixCharactere;
		characteresType[3]=poncuationCharactere;
		
		for(i in characteresType){
			if(characteresType[i]>1){ strength+=4;}
		}
		
		$('span#passwordStrength').removeClass('good bad medium');
		if(strength<NOT_SECURED_LEVEL){
			$('span#passwordStrength').addClass('bad');
			fieldText="NotSecured"
		}else if (strength<SECURED_LEVEL){
			fieldText="NotWellSecured ";
			$('span#passwordStrength').addClass('medium');
	}else{
		fieldText="Secured"
		$('span#passwordStrength').addClass('good');
	}
			$('span#passwordStrength').text(l(fieldText));
		},
		
		
	/* Tests if the form is filled correctly and sends the data */
	_sendUserData: function() {
		var validForm = $('form#user-form').validate({errorElement: 'span'}).form();
		validForm=validForm&&editUser._arePwdEgals(); /* If password are not egals then form is not valid*/
		if (validForm) {
			this._formToSession();
			var user = this.options.context.session('tempUser');
			this._securedPost( 'api/user', this._endOfProcess, $.toJSON(user));
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
		//console.log(user);
		// Cleans the tempUser in session
		this.options.context.session('tempUser', null);
		this.options.context.redirect('#/user/details/' + user.login);
	}
};

$.widget("booking.editUser", $.resthub.resthubController, editUser);
})(jQuery);
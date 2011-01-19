(function($){
    /**
     * Used to edit a user
     *
     */
    var editPassword = {
        options: {
            template: URLS["templateUserPassword"],
            context: null,
            groups: null,
			login: null
        },
        _init: function(){
            this._prepareData();
        },
        /**loads the data needed to display the user*/
        _prepareData: function(){
           this._displayUserForm();
         
        },
       /**Displays and renders the User Form*/
        _displayUserForm: function(){
            /**
             * We load the user from the session data.
             * It is set only if we have to EDIT a user a not for a creation
             */
            var user = this.options.context.session('tempUser');
            this.options.login = user.login;
            this.element.render(this.options.template, {
                user: user,
                groups: this.options.groups
            });
                    
            this._sessionToForm();
            
            $('form#user-form').validate({
                errorElement: 'span'
            });
            
            $('input#user-proceed').unbind();
            $('input#user-proceed').bind('click', $.proxy(this._sendUserData, this));
            
                $('input#password').unbind();
                $('input#password').bind('keyup', this._evalPwd);
                
                /** We want the user to be sure about his password (which is hidden), so we ask it a second time*/
                $('input#passwordCheck').unbind();
                $('input#passwordCheck').bind('keyup', this._evalPwdAreEgals);
        },
        /**Evaluates the requirements on passwords*/
        _evalPwd: function(){
            editPassword._evalPwdStrength();
            /** The audit of the similarity of the passwords is done when we change a password fields*/
            editPassword._evalPwdAreEgals();
        },
        
        /** checks if text inside the passwords input fields are egals or not
         @return boolean */
        _arePwdEgals: function(){
            var pwd1 = $('input[name=password]').val();
            var pwd2 = $('input[name=passwordCheck]').val();
            
            return (pwd1 == pwd2) ? true : false;
        },
        
        
        
        /** change text and class {good,bad} of the passwordsAreEgals span 
         * depending if the text inside the password input fields are egals or not*/
        _evalPwdAreEgals: function(){
        
            var fieldText;
            $('span#passwordsAreEgals').removeClass('good bad');
            var arePwdEgalsVar = editPassword._arePwdEgals();
            if (arePwdEgalsVar) {
                $('span#passwordsAreEgals').addClass('good');
                fieldText = "PasswordsEgals";
            }
            else {
                $('span#passwordsAreEgals').addClass('bad');
                fieldText = "PasswordsNotEgals";
            }
            $('span#passwordsAreEgals').text(l(fieldText));
        },
        
        /** Evaluate strength of the text in the password input field as a password
         * change text {Secured,Not Well Secured,NotSecured} and  class of the passwordStrength span*/
        _evalPwdStrength: function(){
            const NOT_SECURED_LEVEL = 12;
            const SECURED_LEVEL = 20;
            var text = $('input[name=password]').val();
            var strength = 0;
            var capitalLetter = 0;
            var smallLetter = 0;
            var digixCharactere = 0;
            var poncuationCharactere = 0;
            var characteresType = new Array();
            var c = 0;
            
            if (text.length > 5) {
                strength += Math.min(10, text.length);
            }
            /**Categorized each characters and count apparition of each category*/
            for (characterIndex = 0; characterIndex < text.length; characterIndex = characterIndex + 1) {
                charactere = text.charAt(characterIndex);
                if ((charactere >= 'A') && (charactere <= 'Z')) {
                    capitalLetter = capitalLetter + 1;
                    continue;
                }
                
                if ((charactere >= 'a') && (charactere <= 'z')) {
                    smallLetter = smallLetter + 1;
                    continue;
                }
                
                if ((charactere >= '0') && (charactere <= '9')) {
                    digixCharactere = digixCharactere + 1;
                    continue;
                }
                
                if ("!@#$%^&*()_+-='\";:[{]}\|.>,</?`~".indexOf(charactere) >= 0) {
                    poncuationCharactere = 1 + poncuationCharactere;
                    continue;
                }
            }
            
            characteresType[0] = capitalLetter;
            characteresType[1] = smallLetter;
            characteresType[2] = digixCharactere;
            characteresType[3] = poncuationCharactere;
            
            for (var type in characteresType) {
                if (characteresType[type] > 1) {
                    strength += 4;
                }
            }
            
            $('span#passwordStrength').removeClass('good bad medium');
            if (strength < NOT_SECURED_LEVEL) {
                $('span#passwordStrength').addClass('bad');
                fieldText = "NotSecured";
            } else {
                if (strength < SECURED_LEVEL) {
                    fieldText = "NotWellSecured ";
                    $('span#passwordStrength').addClass('medium');
                } else {
                    fieldText = "Secured";
                    $('span#passwordStrength').addClass('good');
                }
            }
            $('span#passwordStrength').text(l(fieldText));
        },
        
        
        /** Tests if the form is filled correctly and sends the data */
        _sendUserData: function(){
            var validForm = $('form#user-form').validate({
                errorElement: 'span'
            }).form();
            validForm = validForm && editPassword._arePwdEgals(); /** If password are not egals then form is not valid*/
            var answer =  confirm(l("confirmUserPasswordModification"));
			if (validForm&&answer) {
                this._formToSession();
                var user = this.options.context.session('tempUser');
                this._securedPost(URLS["apiUserPassword"], this._endOfProcess, $.toJSON(user));
            }
        },
        /** Puts form data in session */
        _formToSession: function(){
            var user = {};
                   
            user.login = this.options.login;
            user.password = $('input[name=password]').val();         
            this.options.context.session('tempUser', user);
        },
        
        /** Displays session data in user form */
        _sessionToForm: function(){
            var user = this.options.context.session('tempUser');    
        },
        
        /**Cleans the temporary object and redirect to the view of the edited User
         * @param {User} user
         * the edited user
         */
        _endOfProcess: function(user){
            // Cleans the tempUser in session
            this.options.context.session('tempUser', null);
            this.options.context.redirect('#/user/details/' + user.login);
        }
    };
	var self=this;
    /**Variable used for text localization with l10n*/
    var l = function(string){
        return string.toLocaleString();
    };
    $.widget("identity.editPassword", $.resthub.resthubController, editPassword);
})(jQuery);

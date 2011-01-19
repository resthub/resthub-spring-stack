(function($){
    /**Used to log the user after the login form has been submited*/
    var postLoginUsers = {
        options: {
            template: URLS["templateUserHome"],
            context: null,
            sampleId: null,
            
            userLogin: null,
            userPassword: null,
            redirectURL: null,
            OAuth2EndPoint: null
        },
        /**tries to authenticate the user on the OAuth2EndPoint*/
        _init: function(){
			/** Needed for context propagation*/
            var self = this;
            
            $.oauth2Conf.tokenEndPoint = self.options.OAuth2EndPoint;
            this._authenticate(self.options.userLogin, self.options.userPassword, function(){
                self._securedGet(URLS["apiUserCurrentUser"], this._displayHome);
            }, function(){
                alert(l("UserLoginFailed"));
                //TODO, change for jquery alert  
            });
        },
        /**Displays and renders the Home of user once the authentication success*/
        _displayHome: function(user){
            this.element.render(this.options.template, {
                user: user
            });
        }
    };
    $.widget("user.postLoginUsers", $.resthub.resthubController, postLoginUsers);
    /** Variable used for text localization with l10n */
	var l = function(string){ return string.toLocaleString(); };
})(jQuery);

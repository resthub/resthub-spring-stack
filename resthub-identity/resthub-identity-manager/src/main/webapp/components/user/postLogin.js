(function($){

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
        _init: function(){
            var self = this;
            
            $.oauth2Conf.tokenEndPoint = self.options.OAuth2EndPoint;
            this._authenticate(self.options.userLogin, self.options.userPassword, function(){
              	self._securedGet(URLS["apiUserCurrentUser"], this._displayHome);
			}, function(){
                alert("We got an Error durring authentication, sorry");
				//TODO, change for jquery alert  
            })
        },
        _displayHome: function(user){
            this.element.render(this.options.template, {
                user: user
            });
        }
    }
    $.widget("user.postLoginUsers", $.resthub.resthubController, postLoginUsers);
    
})(jQuery);

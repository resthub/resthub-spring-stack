(function($){

    var postLoginUsers = {
        options: {
            template: 'components/user/Home.html',
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
               //this.options.context.session(
			    //var info = {};
                //info.login = self.options.userLogin;
                //info.password = self.options.userPassword;
                //self._securedPost('api/user/login/', this._displayHome, $.toJSON(info));
             	self._securedGet('api/user/me', this._displayHome);
			}, function(){
                alert("We got an Error durring authentication, sorry");
                
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

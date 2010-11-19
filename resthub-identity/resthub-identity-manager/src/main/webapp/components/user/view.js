(function($){

    var viewUser = {
        options: {
            userLogin: null,
            template: URLS["templateUserView"],
            context: null
        },
        _init: function(){
            this._securedGet('api/user/login/' + this.options.userLogin, this._displayUser);
        },
        _displayUser: function(user){
            this.element.render(this.options.template, {
                user: user
            });
        }
    };
    
    $.widget("identity.viewUser", $.resthub.resthubController, viewUser);
})(jQuery);

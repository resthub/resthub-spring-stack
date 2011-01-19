(function($){
    /**Used to logout the user*/
    var logoutUser = {
        options: {
            context: null
        },
        /**Logouts the user*/
        _init: function(){
            var self = this;
            self._logout();
			this.options.context.session('tempUser', null);
        }
    };
    
    $.widget("user.logoutUser", $.resthub.resthubController, logoutUser);
    
})(jQuery);

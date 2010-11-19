(function($){
    /**Used to logout the user*/
    var logoutUser = {
        options: {
            context: null,
        },
        /**Logouts the user*/
        _init: function(){
            var self = this;
            self._logout();
        }
    };
    
    $.widget("user.logoutUser", $.resthub.resthubController, logoutUser);
    
})(jQuery);

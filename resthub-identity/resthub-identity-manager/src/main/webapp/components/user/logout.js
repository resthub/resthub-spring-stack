(function($){

    var logoutUser = {
        options: {
            context: null,
        },
        _init: function(){
            var self = this;
            self._logout();
        }
    };
    
    $.widget("user.logoutUser", $.resthub.resthubController, logoutUser);
    
})(jQuery);

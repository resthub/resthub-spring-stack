(function($){

    var loginUsers = {
        options: {
            template: URLS["templateUserLogin"],
            context: null,
            sampleId: null
        },
        _init: function(){
            var self = this;
            self.element.render(self.options.template);
        }
    };
    
    $.widget("user.loginUsers", $.resthub.resthubController, loginUsers);
    
})(jQuery);

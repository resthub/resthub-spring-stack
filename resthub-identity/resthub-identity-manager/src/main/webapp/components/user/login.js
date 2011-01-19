(function($){
    /**Used to display login form*/
    var loginUsers = {
        options: {
            template: URLS["templateUserLogin"],
            context: null,
            sampleId: null
        },
        /**Display and render the login Form*/
        _init: function(){
            var self = this;
            self.element.render(self.options.template);
        }
    };
    
    $.widget("user.loginUsers", $.resthub.resthubController, loginUsers);
    /** Variable used for text localization with l10n */
	var l = function(string){
        return string.toLocaleString();
    };
})(jQuery);

(function($){
    /**Used to view a User*/
    var viewUser = {
        options: {
            userLogin: null,
            template: URLS["templateUserView"],
            context: null
        },
        /**Gets the user data*/
        _init: function(){
            this._securedGet('api/user/login/' + this.options.userLogin, this._displayUser);
        },
        /**Displays and renders the user
         * 
         * @param {User} user
         * the user to display
         */
        _displayUser: function(user){
            this.element.render(this.options.template, {
                user: user
            });
        }
    };
	/** Variable used for text localization with l10n */
    var l = function(string){ return string.toLocaleString()};
    $.widget("identity.viewUser", $.resthub.resthubController, viewUser);
})(jQuery);

(function($){
    /**Used to view a Group*/
    var viewGroup = {
        options: {
            groupName: null,
            template: URLS["templateGroupView"],
            context: null
        },
        /**
         * Load's the data needed
         */
        _init: function(){
            this._securedGet(URLS["apiGroupByName"] + this.options.groupName, this._displayGroup);
        },
        /**
         * Displays and renders the Group
         *
         * @param {Group} group
         */
        _displayGroup: function(group){
            this.element.render(this.options.template, {
                group: group
            });
            
            $('table tr:nth-child(even)').addClass('striped');
            
            var self = this;
            /* Link 'remove' action on _removeUser function */
            $('span.remove-user').each(function(index, element){
                $(element).click(function(){
                    self._removeUser($(element).attr('id'));
                });
            });
        },
        /**
         * Remove the user from the Group
         * @param {String} userLogin
         * the login of the user to remove from the Group
         */
        _removeUser: function(userLogin){
            this._securedDelete(URLS["apiGroup"] + this.options.groupName + '/user/' + userLogin, this._userRemoved);
        },
        /**
         * Callback function when the users has been removed from the Group
         * @param {Group} updatedGroup
         */
        _userRemoved: function(updatedGroup){
            $.pnotify({
                pnotify_title: 'Information',
                pnotify_text: 'User deleted successfully from group ' + updatedGroup.name + '.'
            });
            this._displayGroup(updatedGroup);
        }
    };
    var l = function(string){ return string.toLocaleString()};
    $.widget("identity.viewGroup", $.resthub.resthubController, viewGroup);
})(jQuery);

(function($){
    /** Used for edition of Group in the webapps */
    var editGroup = {
        options: {
            template: URLS["templateGroupEdit"],
            context: null,
        },
        _init: function(){
 			this._displayGroupForm();
        },

         /**Displays and render the Group form*/
        _displayGroupForm: function(){
            var group = this.options.context.session('tempGroup');
            
            this.element.render(this.options.template, {
                group: group
            });
            
            $('#content h1:first').html(l("GroupCreate"));
            
            this._sessionToForm();
            
            $('form#group-form').validate({
                errorElement: 'span'
            });
            
            $('input#group-proceed').unbind();
            $('input#group-proceed').bind('click', $.proxy(this._sendGroupData, this));
            
        },
        
        /** Tests if the form is filled correctly and sends the data */
        _sendGroupData: function(){
            var validForm = $('form#group-form').validate({
                errorElement: 'span'
            }).form();
            if (validForm) {
                this._formToSession();
                var group = this.options.context.session('tempGroup');
                this._securedPost(URLS["apiGroup"], this._endOfProcess, $.toJSON(group));
            }
        },
        /** Puts form data in session */
        _formToSession: function(){
            var group = {
                permissions: []
            };
            group.name = $('input[name=name]').val();          
            this.options.context.session('tempGroup', group);
        },
        /** Displays session data in user form */
        _sessionToForm: function(){
            var group = this.options.context.session('tempGroup');
            if (group != null) {
                $('input[name=name]').val(group.name);
            }
        },
        /**
         * Cleans the temporary objects when everything done and redirect to the group details view
         * @param {Group} group
         * the edited Group
         */
        _endOfProcess: function(group){
            //console.log(user);
            // Cleans the tempUser in session
            this.options.context.session('tempGroup', null);
            this.options.context.redirect('#/group/list');
        }
    };
    var l = function(string){
        return string.toLocaleString()
    };
    
    $.widget("booking.editGroup", $.resthub.resthubController, editGroup);
})(jQuery);

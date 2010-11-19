(function($){
    /**Used when listing the Groups*/
    var listGroups = {
        options: {
            template: URLS["templateGroupList"],
            context: null,
            page: 0
        },
        /**Init's the swichPage mode*/
        _init: function(){
            this._switchPage(this.options.page);
        },
        /**Displays and renders group list*/
        _displayGroups: function(result){
            this.element.render(this.options.template, {
                result: result
            });
            
            var self = this;
            $('span.page-switcher').each(function(index, element){
                $(element).click(function(){
                    var page = $(this).attr('id').split("-")[1];
                    self._switchPage(page);
                });
            });
            
            $("table tr:nth-child(even)").addClass("striped");
        },
        /**
         * Switchs the page
         *
         * @param {Integer} page
         * the page to be displayed
         */
        _switchPage: function(page){
            this.options.page = page;
            this._securedGet(URLS["apiGroup"] + '?page=' + this.options.page, this._displayGroups);
        }
    };
    
	var l = function(string){ return string.toLocaleString()};
	
    $.widget("identity.listGroups", $.resthub.resthubController, listGroups);
})(jQuery);

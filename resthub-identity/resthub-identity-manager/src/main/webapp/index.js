
(function($){
    var index = {
        options: {
            context: null,
            template: URLS["templateMain"]
        },
        _init: function(){
            $('#content').render(this.options.template);
        }
    };
    var l = function(string){
        return string.toLocaleString()
    };
    
    $.widget("identity.Main", $.resthub.resthubController, index);
    
    $(function(){
        index._init();
    });
})(jQuery);


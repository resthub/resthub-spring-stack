jQuery.session || (function($) {

    if(!sessionStorage) {
        throw('sessionStorage support is required');
    }

    $.session = {

        setJSONItem : function(key, value){
            sessionStorage.setItem(key, $.toJSON(value));
        },

        getJSONItem : function(key){
            var item = sessionStorage.getItem(key);
            if(item) {
                return $.parseJSON(item);
            } else {
                return {};
            }
          },
          
         clear : function(){
            sessionStorage.clear();
          }
    };

 })(jQuery);
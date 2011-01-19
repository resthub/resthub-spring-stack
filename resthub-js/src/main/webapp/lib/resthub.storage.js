jQuery.storage || (function($) {

    if(!localStorage) {
        throw('localStorage support is required');
    }

    $.storage = {

        /**
		 * Store an item in the local storage (Not compatible with Internet Explorer <= 7)
		 * Todo : implement a fallback mechanism for browser without HTML5 localstorage capabilities, like Internet Explorer <= 7
		 *
		 * @param {String} key Key of the stored item, this will be used to retreive it later
		 * @param {Object} item Item than will be stored in the local storage
		 **/
		setItem : function(key, item){
            localStorage.setItem(key, $.toJSON(item));
        },

		/**
		 * Retreive an item from the local storage
		 * Todo : implement a fallback mechanism for browser without HTML5 localstorage capabilities, like Internet Explorer <= 7
		 *
		 * @param {String} key Key of the item to retreive
		 * @return {Object} The object retreive
		 **/
        getItem : function(key){
            var item = localStorage.getItem(key);
            if(item) {
                return $.parseJSON(item);
            } else {
                return {};
            }
          },
          
		 
          /**
           * Clear local storage
           */
         clear : function(){
            localStorage.clear();
          }
    };

 })(jQuery);
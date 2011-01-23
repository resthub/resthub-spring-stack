define([ 'jquery', 'jquery.json' ], function() {
(function($) {

    if(!localStorage) {
        throw('localStorage support is required');
    }

    /**
     * Abstract various browser storage methods
     * TODO actually juste localstorage is implemented, we should implement other
     * storage mechanisms (memory, jquery data, session storage, cookie). We should backport some code from
     * the awesome Sammy.Storage plugin (https://github.com/quirkey/sammy/raw/master/lib/plugins/sammy.storage.js)
     * 
     */
    $.storage = {

		/**
		 * Store an item in the local storage (Not compatible with Internet Explorer <= 7)
		 *
		 * @param {String} key Key of the stored item, this will be used to retreive it later
		 * @param {Object} item Item than will be stored in the local storage, can be a string or an object
		 **/
    	set : function(key, item){
			var string_value = (typeof item == 'string') ? item : JSON.stringify(item);
			localStorage.setItem(key, string_value);
        },
        
        /**
		 * Retreive an item from the local storage
		 *
		 * @param {String} key Key of the item to retreive
		 * @return {Object} The object retreived
		 **/
        get : function(key){
        	var value = localStorage.getItem(key);
            if (typeof value == 'undefined' || value == null || value == '') {
              return value;
            }
            try {
              return $.evalJSON(value);
            } catch(e) {
              return value;
            }
        	
         },
          
          /**
           * Clear all items currently stored
           */
         clear : function(){
            localStorage.clear();
         },
          
         /**
          * Remove the specified item 
          * @param key Key of the item to remove
          */
         remove : function(key){
            localStorage.removeItem(key);
         }
            
    };

 })(jQuery);
});
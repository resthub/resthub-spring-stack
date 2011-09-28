define([ 'text!poll/search.html', 'lib/controller'], function(tmpl, Controller) {

    return Controller.extend("SearchPollController", {
        template : tmpl,

        init : function() {
            var self = this;
            
            this.render();

            this.element.find('form').bind('submit', function(e) {
		var form = $(this);
		var location = form.attr('action');
                location += '?' + self._serializeFormParams(form);

                $.route(location);

                if (e.preventDefault) {
                    e.preventDefault();
                }
                e.returnValue = false;
                return e.returnValue;
            });
        },
        _serializeFormParams : function(form) {
            var queryString = "", fields = form.serializeArray();
            if (fields.length > 0) {
                for (var i = 0; i < fields.length; i++) {
                    var key = encodeURIComponent(fields[i].name);
                    var val = encodeURIComponent(fields[i].value);
                    queryString = queryString + ( i==0 ? "" : "&") + key + '=' + val;
                }
            }
            return queryString;
        }
    });
});

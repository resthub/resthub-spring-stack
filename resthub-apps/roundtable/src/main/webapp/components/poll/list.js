(function($) {
    var listPolls = {
        _template : 'components/poll/list.html',
        _query: null,
        _page : 0,
        options: {
            context : null,
            url: 'api/poll/search'
        },
        _create: function() {
            this.element.addClass('rt-poll-list');
        },
        _init: function() {
            this._context().title('Round Table - Poll list');
        },
        destroy: function() {
            this.element.removeClass('rt-poll-list');
            $.Widget.prototype.destroy.call(this);
        },
        _context: function() {
            return this.options.context;
        },
        _url: function() {  
            return this.options.url;
        },
        _updatePolls: function(polls) {
            var self = this;
            this.element.render(this._template, {
                polls: polls,
                query: this._query,
                page: this._page
            });

            this.element.find('li.poll-item').click(function() {
                var id = $(this).attr('id').split("-")[1];
                self._context().redirect('#/poll', id);
            });

            this._trigger("change");
        },

        search: function(query, page) {
            this._query = query;
            this._page = page;

            var params = '?page=' + this._page;
            if (this._query != null) {
                params += "&q=" + this._query;
            }
            var url = this._url() + params;

            this._get(url, this, '_updatePolls');
            return this;
        }
    };

    $.widget("roundtable.listPolls", $.resthub.resthubController, listPolls);
})(jQuery);


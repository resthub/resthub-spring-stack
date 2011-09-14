define([ 'text!poll/list.html', 'lib/controller', 'repositories/poll.repository' ], function(tmpl, Controller, PollRepository) {

    return Controller.extend("ListPollController", {
        template : tmpl,
        query: '',
        page: 0,

        init : function() {
            PollRepository.find($.proxy(this, '_displayPolls'), this.query, this.page);
        },
        _displayPolls : function(page_data) {
            this.render({
		page: page_data,
		query: this.query
            }, {
                getPages: function() {
                    return new Array(page_data.totalPages);
                },
                formatDate: function(date) {
                    var d = new Date(date);
                    return d.toDateString();
                }
            });

            // HACK until HTML5 href on LI will be support
            this.element.find('li.poll-item').click(function() {
		$.route($(this).attr('href'));
            });
        }
    });
});
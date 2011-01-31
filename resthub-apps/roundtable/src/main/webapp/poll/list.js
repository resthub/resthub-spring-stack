define([ 'lib/controller', 'repositories/poll.repository' ], function(Controller, PollRepository) {

    return Controller.extend("ListPollController", {
        template : 'poll/list.html',
        query: null,
        page: 0,

        init : function() {
            PollRepository.find($.proxy(this, '_displayPolls'), this.query, this.page);
        },
        _displayPolls : function(page_data) {
            this.render({
		page: page_data,
		query: this.query
	    });

            // HACK until HTML5 href on LI will be support
            this.element.find('li.poll-item').click(function() {
		$.route($(this).attr('href'));
            });
        }
    });
});
define([ 'poll/list', 'poll/view', 'poll/search', 'poll/edit' ], function() {

    /* BEGIN EVENTS */
    $.subscribe('voted', function() {
        $.pnotify('Vote done.');
    });
    $.subscribe('poll-created', function() {
        $.pnotify('Poll created with success.');
    });
    /* END EVENTS */

    /**
     * View polls.
     */
    $.route('#/poll', function(params) {
        console.debug('#/poll');

        var page = (params['page'] != undefined) ? params['page'] : 0;
        var query = (params['q'] != undefined) ? params['q'] : null;

        $('#content').list_poll({query: query, page: page});
    });

    /**
     * View new poll creation form.
     */
    $.route('#/poll/new', function(params) {
        console.debug('#/poll/new');
        $('#content').edit_poll();
    });

    /**
     * View poll identified by 'id.
     */
    $.route('#/poll/:id', function(params) {
        console.debug('#/poll/:id with id = ' + params.id);
        var poll = { id: params.id }
        $('#content').view_poll({poll : poll});
    });
});

define([ 'lib/repository' ], function(Repository) {

    return Repository.extend("PollRepository", {
        root : 'api/poll/',

        find : function(callback, query, page) {
            var url = this.root + 'search?page=' + page;
            if (query) {
                url = url + '&q=' + query;
            }
            this._get(url, callback);
        },

        vote : function(callback, voter, pid, values) {
            $.ajax({
                url : 'api/vote',
                dataType : 'json',
                type : 'POST',
                data : {voter: voter, pid:pid, values: values},
                success : callback
            });
        }
    }, {});
});

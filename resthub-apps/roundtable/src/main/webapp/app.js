/**
 * Main APP
 */
define([ 'lib/resthub', 'routes/poll.routes', 'lib/jquery/jquery.pnotify' ],
    function($, PollRoutes, Controller) {
        $(document).ready(function() {
		
            $.route('#/', function() {
                $('#content').html('<p class="welcome">Welcome to <strong>Roundtable :)</strong></p>');
            });

            // Rebuild Lucene index
            $.ajax({
                url : 'api/lucene/rebuild',
                dataType : 'text',
                type : 'POST'
            });

            $('body').ajaxError(function(e, xhr, settings, exception) {
                if (xhr.status == 404) {
                    $.pnotify({
                        pnotify_title: 'Resource not found.',
                        pnotify_text: 'No objects found.'
                    });
                }
                else {
                    $.pnotify({
                        pnotify_title: 'Server trouble!',
                        pnotify_text: 'Error requesting ' + settings.url,
                        pnotify_type: 'error',
                        pnotify_hide: false
                    });
                }
            });

            $('#search').search_poll();
		
            $.route(location.hash);
        });
    }
);
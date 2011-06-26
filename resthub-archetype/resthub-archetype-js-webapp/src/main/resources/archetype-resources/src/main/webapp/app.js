define(['lib/resthub', 'sample/list.js'], function() {
        
    // Define routes
    $.route('#', function() {
        $('#main').html('<span>Home</span>');
    });
            
    $.route('#/sample', function() {
        $('#main').list_samples();
    });
    
    // Run current route
    $.route(location.hash);

});
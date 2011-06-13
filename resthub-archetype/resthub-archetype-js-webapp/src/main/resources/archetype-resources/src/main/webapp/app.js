define(["lib/route"], function() {

    $(document).ready(function(){
        
        // Define routes
        $.route('#', function() {

            $('#main').html('<span>Home</span>');
        });
                
        $.route('#/test', function() {
            $('#main').html('<span>Hello you</span>');
        });
        
        // Run current route
        $.route(location.hash);
                
    });

});
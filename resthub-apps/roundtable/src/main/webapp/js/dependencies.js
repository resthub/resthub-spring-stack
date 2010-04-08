JS.Packages(function() { with(this) {

        file('/js/index.js').provides('Index');
        file('/js/component/poll/create.js').provides('Poll.Create')
                                     .requires('Index','JS.Class');
        file('/js/component/poll/view.js').provides('Poll.View')
        							 .requires('Index','JS.Class');
		}

        
    });
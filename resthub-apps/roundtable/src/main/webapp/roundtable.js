/**
 * Round Table creation component.
 */
var RoundTableCreateComponent = $.Class.create({
    // constructor
    initialize: function(anchor) {
        this._anchor = anchor;
        this.template = new EJS({url: 'template/poll/create.ejs'});

        var tpl = '<li>' +
            '<div><input type="checkbox" name="answers" value="<%= answer %>"></div>' +
            '<span><%= answer %></span>' +
            '</li>';
        var answerTemplate = new EJS({text:tpl});


        var poll = {
            "id": null, "author": null, "topic": "New poll", "body": "",
            "answers": [
                {body: "Lorem ipsum dolor sit amet,"},
                {body: "consectetur adipisicing elit,"},
                {body: "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."}]
        };

        this.template.update(this._anchor, poll);

        $('textarea.resizable:not(.processed)').TextAreaResizer();
        $('#create-rt form').validate({
            errorPlacement: function(error, element) {
                if (element.hasClass('resizable')) {
                    error.insertAfter(element.next());
                }
                else {
                    error.insertAfter(element);
                }
            }
        });
        $('#create-rt-answers').sortable({
            placeholder: 'answer-placeholder'
        });
        $('#create-rt-answers').disableSelection();

        $('#inp-del-answer').click(function() {
            $('input:checkbox[name=answers]:checked').parents('li').remove();
        });

        $('#inp-add-answer').click(function() {
            var answer = $('#inp-answer').val().trim();
            if (answer != '') {
                $("#create-rt-answers").append(
                    answerTemplate.render({answer:answer}));
                $("#inp-answer").val("");
            }
        });
    },
    // methods
    toString: function() {
        return "";
    }
}, {
    // properties
    getset: [['Anchor', '_anchor']]
});

/**
 * Round Table visualization component.
 */
var RoundTableViewComponent = $.Class.create({
    // constructor
    initialize: function(anchor, poll) {
        this._anchor = anchor;
        this.template = new EJS({url: 'template/poll/view.ejs'});

        this.template.update(this._anchor, poll);
    },
    // methods
    toString: function() {
        return "";
    }
}, {
    // properties
    getset: [['Anchor', '_anchor']]
});

/**
 * ROUTES
 */
(function($) {
    var app = $.sammy(function() {
        this.get('#/', function() {
            $('#main').text('');
        });

        /**
         * View the test poll.
         */
        this.get('#/test', function() {
            $.ajax({
                url: 'data/poll/test.json',
                dataType: 'json',
                success: function(poll) {
                    new RoundTableViewComponent('main', poll);
                }
            });
        });

        /**
         * View poll.
         */
        this.get('#/poll/:id', function() {
            $.ajax({
                url: 'webresources/poll/' + params['id'],
                dataType: 'json',
                success: function(poll) {
                    new RoundTableViewComponent('main', poll);
                }
            });
        });

        /**
         * View new poll creation form.
         */
        this.get('#/create', function() {
            new RoundTableCreateComponent('main');
        });

        /**
         * Post a new poll.
         */
        this.post('#/post/poll', function(context) {
            var poll = {
                author: this.params['author'],
                topic: this.params['topic'],
                body: this.params['body'],
                answers: []
            }
            // FIXME found another way...
            var answers = this.params.$form[0].answers;
            for (i = 0; i < answers.length; i++) {
                poll.answers.push({body:answers[i].value});
            }
            
            $.ajax({
                type: 'POST',
                url: 'webresources/poll',
                contentType: 'application/json',
                dataType: 'json',
                processData: false,
                data: poll,
                success: function(poll) {
                    context.redirect('#/poll/', poll.id);
                }
            });
        });
    });

    $(function() {
        app.run();
    });
})(jQuery);

define([
    'lib/controller',
    'repositories/poll.repository',
    'lib/fileuploader',
    'lib/jquery/jquery.textarearesizer',
    'lib/jquery/jquery.validate',
    'lib/jqueryui/sortable'
], function(Controller, PollRepository) {

    return Controller.extend("EditPollController", {
        template : 'poll/edit.html',
        answerTemplate : '<li>' +
                '<div><input type="checkbox" name="answers" value="<%= answer %>"></div>' +
                '<span><%= answer %></span>' +
                '</li>',
        data : {
            id : null,
            author : null,
            topic : 'New poll',
            body : '',
            expirationDate : new Date(),
            answers: []
        },

        init : function() {
            var self = this;

            this.render(this.data);

            this.element.find('textarea.resizable:not(.processed)').TextAreaResizer();
            this.element.find('form').validate({
                errorPlacement: function(error, element) {
                    if (element.hasClass('resizable')) {
                        error.insertAfter(element.next());
                    }
                    else {
                        error.insertAfter(element);
                    }
                }
            });
            /*this.element.find('ul.answers').sortable({
                placeholder: 'answer-placeholder'
            });*/
            this.element.find('ul.answers').disableSelection();

            this.element.find('input:button[name=delete]').click(function() {
                self.element.find('input:checkbox[name=answers]:checked')
                    .parents('li').remove();
            });

            this.element.find('input:button[name=add]').click(function() {
                var answer = self.element.find('textarea[name=answer]').val().trim();
                if (answer != '') {
                    self.element.find('ul.answers').append($.render('',{answer:answer},{text:self.answerTemplate}));
                    self.element.find('textarea[name=answer]').val("");
                }
            });

	    var uploader = new qq.FileUploader({
		// pass the dom node (ex. $(selector)[0] for jQuery users)
		element: document.getElementById('attach-illustration-to-body'),
		// path to server-side upload script
		action: 'api/illustration/upload',
		onComplete: function(id, fileName, responseJSON){
		    $('#attach-illustration-to-body').remove();
		    $('#illustration').attr('alt', fileName);
		    $('#illustration').attr('src', 'api/thumbnail/' + responseJSON.files[0] + '?tmp=true');
		    $('#inp-illustration').val(responseJSON.files[0]);
		    $('#illustration-block').show();
		}
	    });

            this.element.find('form').submit(function() {
		var form = $(this);

                if (!form.valid()) return false;

                var values = {};
                $.each(form.serializeArray(), function(i, field) {
                    values[field.name] = field.value;
                });

                var poll = {
                    author: values['author'],
                    topic: values['topic'],
                    body: values['body'],
                    illustration: values['illustration'],
                    // expirationDate: (new Date(this.params['expirationDate'])).format('Y-m-d'),
                    answers: []
                }
                // FIXME find a cleaner way...
                var answers = form.context.answers;
                for (i = 0; i < answers.length; i++) {
                    poll.answers.push({
                        body:answers[i].value
                    });
                }

                PollRepository.save(function(poll) {
                    $.route('#/poll/' + poll.id);
                    $.publish('poll-created');
                }, $.toJSON(poll));

                return false;
            });
        }
    });
});

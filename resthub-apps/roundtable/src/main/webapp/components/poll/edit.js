(function($) {
    var editPoll = {
        _template : 'components/poll/edit.html',
        _answerTemplate : '<li>' +
                '<div><input type="checkbox" name="answers" value="<%= answer %>"></div>' +
                '<span><%= answer %></span>' +
                '</li>',
        options: {
            data : {
                id : null,
                author : null,
                topic : 'New poll',
                body : '',
                expirationDate : new Date(),
                answers: []
                },
            context : null
        },
        _create: function() {
            this.element.addClass('rt-poll-edit');
        },
        destroy: function() {
            this.element.removeClass('rt-poll-edit');
            $.Widget.prototype.destroy.call( this );
        },
        _init: function() {
            this._context().title('Round Table - Create new poll');

            var self = this;

            this.element.render(this._template, this.options.data);

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
            this.element.find('ul.answers').sortable({
                placeholder: 'answer-placeholder'
            });
            this.element.find('ul.answers').disableSelection();

            this.element.find('input:button[name=delete]').click(function() {
                self.element.find('input:checkbox[name=answers]:checked')
                    .parents('li').remove();
            });

            this.element.find('input:button[name=add]').click(function() {
                var answer = self.element.find('textarea[name=answer]').val().trim();
                if (answer != '') {
                    self.element.find('ul.answers').append($.render('',{answer:answer},{text:self._answerTemplate}));
                    self.element.find('textarea[name=answer]').val("");
                }
            });
        },
        _context: function() {
            return this.options.context;
        }
    };

    $.widget("roundtable.editPoll", $.resthub.resthubController, editPoll);
})(jQuery);
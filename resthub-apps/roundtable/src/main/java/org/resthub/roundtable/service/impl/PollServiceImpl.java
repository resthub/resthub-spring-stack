package org.resthub.roundtable.service.impl;


import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.annotation.Auditable;
import org.resthub.core.service.impl.GenericResourceServiceImpl;
import org.resthub.roundtable.dao.PollDao;
import org.resthub.roundtable.model.Answer;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Poll service implementation.
 * @author Nicolas Carlier
 */
@Named("pollService")
public class PollServiceImpl extends GenericResourceServiceImpl<Poll, PollDao> implements PollService {

    @Inject
    @Named("pollDao")
    @Override
    public void setDao(PollDao pollDao) {
        this.dao = pollDao;
    }

    @Override
    @Auditable
    @Transactional(readOnly = false)
    public Poll create(Poll resource) {
        Calendar date = Calendar.getInstance();

        Poll poll = new Poll();
        poll.setAuthor(resource.getAuthor());
        poll.setBody(resource.getBody());
        poll.setTopic(resource.getTopic());
        poll.setCreationDate(date.getTime());
        poll.setAnswers(new ArrayList<Answer>());
        for (int i = 0; i < resource.getAnswers().size(); i++) {
            Answer a = resource.getAnswers().get(i);
            Answer answer = new Answer();
            answer.setBody(a.getBody());
            answer.setOrder(i + 1);
            answer.setPoll(poll);
            poll.getAnswers().add(answer);
        }

        // Set expiration date if null
        if (resource.getExpirationDate() == null) {
            date.add(Calendar.MONTH, 1);
            poll.setExpirationDate(date.getTime());
        }
        else {
            poll.setExpirationDate(resource.getExpirationDate());
        }

        return super.create(poll);
    }



}

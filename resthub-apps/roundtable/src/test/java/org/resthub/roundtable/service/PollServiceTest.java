package org.resthub.roundtable.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Test;
import org.resthub.core.test.service.AbstractServiceTest;
import org.resthub.roundtable.model.Answer;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.common.ServiceException;

/**
 * Test of Poll services.
 * 
 * @author Nicolas Carlier
 */
public class PollServiceTest extends AbstractServiceTest<Poll, Long, PollService> {

    @Inject
    @Named("pollService")
    @Override
    public void setService(PollService pollService) {
        super.setService(pollService);
    }

    @Override
    protected Poll createTestEntity() {
        Poll poll = new Poll();
        poll.setAuthor("me");
        poll.setBody("test poll");
        poll.setTopic("TEST");

        List<Answer> answers = new ArrayList<Answer>();
        for (int i = 1; i <= 3; i++) {
            Answer answer = new Answer();
            answer.setBody("Answer number " + i);
            answer.setPoll(poll);
            answers.add(answer);
        }

        poll.setAnswers(answers);

        return poll;
    }

    @Override
    @Test
    public void testUpdate() {
        Poll poll = service.findById(this.id);
        poll.setAuthor("somebody");
        poll.getAnswers().remove(1);

        poll = service.update(poll);
        Assert.assertEquals("unable to update Poll", "somebody", poll.getAuthor());
        Assert.assertEquals("Unable to update Poll", 2, poll.getAnswers().size());
    }

    @Test
    public void testFind() throws ServiceException {
        service.rebuildIndex();
        List<Poll> polls = service.find("test", null).getContent();
        Assert.assertEquals(1, polls.size());
    }
}

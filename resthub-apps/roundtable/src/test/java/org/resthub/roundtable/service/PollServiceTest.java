package org.resthub.roundtable.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Test;
import org.resthub.core.test.AbstractResourceServiceTest;
import org.resthub.roundtable.model.Answer;
import org.resthub.roundtable.model.Poll;
import org.springframework.test.context.transaction.TransactionConfiguration;

 /**
 * Test of Poll services.
 * @author Nicolas Carlier
 */
@TransactionConfiguration(defaultRollback = false)
public class PollServiceTest extends AbstractResourceServiceTest<Poll, PollService> {

    @Inject
    @Named("pollService")
    @Override
    public void setResourceService(PollService pollService) {
        super.setResourceService(pollService);
    }

    @Override
    protected Poll createTestRessource() throws Exception {
        Poll poll = new Poll();
        poll.setAuthor("me");
        poll.setBody("test poll");
        poll.setTopic("TEST");

        List<Answer> answers = new ArrayList<Answer>();
        for (int i = 1; i <= 3; i++) {
            Answer answer = new Answer();
            answer.setBody("Answer number " + i);
            answers.add(answer);
        }

        poll.setAnswers(answers);

        return poll;
    }

    @Override
    @Test
    public void testUpdate() throws Exception {
        Poll poll = resourceService.findById(this.resourceId);
        poll.setAuthor("somebody");
        poll.getAnswers().remove(1);

        poll = resourceService.update(poll);
        Assert.assertEquals("unable to update Poll", "somebody", poll.getAuthor());
        Assert.assertEquals("Unable to update Poll", 2, poll.getAnswers().size());
    }

    @Test
    public void testFind() throws Exception {
        resourceService.rebuildIndex();
        List<Poll> polls = resourceService.find("test", null).asList();
        Assert.assertEquals("Unable to find Polls", 1, polls.size());
    }
}

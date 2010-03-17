package org.resthub.roundtable.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.junit.Assert;
import org.junit.Test;
import org.resthub.roundtable.domain.model.Answer;
import org.resthub.roundtable.domain.model.Poll;
import org.resthub.core.service.ResourceService;
import org.resthub.core.test.AbstractResourceServiceTest;

 /**
 * Test of Poll services.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
public class PollServiceTest extends AbstractResourceServiceTest<Poll> {
    @Inject
    @Named("pollService")
    @Override
    public void setResourceService(ResourceService<Poll> resourceService) {
        super.setResourceService(resourceService);
    }

    @Override
    protected Poll createTestRessource() throws Exception {
        Calendar date = Calendar.getInstance();
        date.add(Calendar.MONTH, 1);

        Poll poll = new Poll();
        poll.setAuthor("me");
        poll.setBody("Test poll");
        poll.setExpirationDate(date.getTime());
        poll.setTopic("TEST");
        poll.setType(Poll.Type.MULTI);

        List<Answer> answers = new ArrayList<Answer>();
        for (int i = 1; i <= 3; i++) {
            Answer answer = new Answer();
            answer.setBody("Answer number " + i);
            answer.setOrder(i);
            answer.setCreationDate(new Date());
            answer.setModificationDate(new Date());
            answer.setPoll(poll);
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
}

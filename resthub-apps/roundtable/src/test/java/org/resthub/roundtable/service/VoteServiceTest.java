package org.resthub.roundtable.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.resthub.roundtable.model.Answer;
import org.resthub.roundtable.model.Poll;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

 /**
 * Test of Poll services.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:resthubContext.xml", "classpath:roundtableContext.xml" })
@TransactionConfiguration(defaultRollback = true)
@Transactional(readOnly = false)
public class VoteServiceTest {
    protected VoteService voteService;
    
    protected PollService pollService;
    
    protected Long pollId;

    @Inject
    @Named("voteService")
    public void setVoteService(VoteService voteService) {
        this.voteService = voteService;
    }
    
    @Inject
    @Named("pollService")
    public void setPollService(PollService pollService) {
        this.pollService = pollService;
    }

    @Before
    public void setUp() throws Exception {
        Poll poll = new Poll();
        poll.setAuthor("me");
        poll.setBody("Test poll");
        poll.setTopic("TEST");

        List<Answer> answers = new ArrayList<Answer>();
        for (int i = 1; i <= 3; i++) {
            Answer answer = new Answer();
            answer.setBody("Answer number " + i);
            answers.add(answer);
        }

        poll.setAnswers(answers);
        poll = this.pollService.create(poll);
        this.pollId = poll.getId();
    }

    @Test
    public void testVote() throws Exception {
        List<String> values = new ArrayList<String>();
        values.add("oui");
        values.add("non");
        values.add("oui");
        
        this.voteService.vote("JUNIT", this.pollId, values);

        Poll poll = this.pollService.findById(this.pollId);

        Assert.assertEquals("Unable to vote for Poll", 1, poll.getVoters().size());
    }
}

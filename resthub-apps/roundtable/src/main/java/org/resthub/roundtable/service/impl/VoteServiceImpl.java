package org.resthub.roundtable.service.impl;


import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.resthub.core.annotation.Auditable;
import org.resthub.roundtable.dao.VoteDao;
import org.resthub.roundtable.dao.VoterDao;
import org.resthub.roundtable.model.Answer;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.model.Vote;
import org.resthub.roundtable.model.Voter;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.VoteService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Vote service implementation.
 * @author Nicolas Carlier
 */
@Named("voteService")
public class VoteServiceImpl implements VoteService {

    private PollService pollService;
    
    private VoteDao voteDao;

    private VoterDao voterDao;

    @Inject
    @Named("pollService")
    public void setPollService(PollService pollService) {
        this.pollService = pollService;
    }
    
    @Inject
    @Named("voteDao")
    public void setVoteDao(VoteDao voteDao) {
        this.voteDao = voteDao;
    }

    @Inject
    @Named("voterDao")
    public void setVoterDao(VoterDao voterDao) {
        this.voterDao = voterDao;
    }

    @Override
    @Auditable
    @Transactional(readOnly = false)
    public void vote(String voterName, Long pid, List<String> values) {
        Poll poll = this.pollService.findById(pid);

        Assert.notNull(poll, "Poll not found");
        Assert.isTrue(values.size() == poll.getAnswers().size(), "Votes doesn't matches with answers");

        Voter voter = this.voterDao.findByNameAndPoll(voterName, poll);
        if (voter == null) {
            // create voter
            voter = new Voter();
            voter.setName(voterName);
            voter.setPoll(poll);
            poll.getVoters().add(voter);

            voter = this.voterDao.saveAndFlush(voter);
        }

        for (Answer answer : voter.getPoll().getAnswers()) {
            Vote vote = new Vote();
            vote.setAnswer(answer);
            vote.setVoter(voter);
            vote.setValue(values.get(answer.getOrder() - 1));

            this.voteDao.saveAndFlush(vote);
        }
    }

}

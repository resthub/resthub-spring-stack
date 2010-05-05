package org.resthub.roundtable.service.impl;


import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.resthub.core.annotation.Auditable;

import org.resthub.roundtable.domain.dao.VoteDao;
import org.resthub.roundtable.domain.model.Answer;
import org.resthub.roundtable.domain.model.Poll;
import org.resthub.roundtable.domain.model.Vote;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.VoteService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Vote service implementation.
 * @author Nicolas Carlier (mailto:pouicbox@yahoo.fr)
 */
@Named("voteService")
public class VoteServiceImpl implements VoteService {

    private PollService pollService;

    private VoteDao voteDao;

    @Inject
    @Named("voteDao")
    public void setResourceDao(VoteDao voteDao) {
        this.voteDao = voteDao;
    }

    @Inject
    @Named("pollService")
    public void setPollService(PollService pollService) {
        this.pollService = pollService;
    }

    @Override
    @Auditable
    @Transactional(readOnly = false)
    public Poll vote(String voter, Long pid, List<String> values) {
        Poll poll = this.pollService.findById(pid);

        Assert.notNull(poll, "Poll not found");
        Assert.isTrue(values.size() == poll.getAnswers().size(), "Votes don't matches with answers");

        for (Answer answer  : poll.getAnswers()) {
            Vote vote = new Vote();
            vote.setAnswer(answer);
            vote.setPoll(poll);
            vote.setValue(values.get(answer.getOrder()));
            vote.setVoter(voter);

            this.voteDao.save(vote);
        }

        return this.pollService.findById(pid);
    }

}

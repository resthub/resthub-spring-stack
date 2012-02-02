package org.resthub.roundtable.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.lucene.queryParser.ParseException;
import org.resthub.core.service.GenericServiceImpl;
import org.resthub.roundtable.repository.PollRepository;
import org.resthub.roundtable.model.Answer;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.common.ServiceException;
import org.resthub.roundtable.toolkit.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * Poll service implementation.
 * 
 * @author Nicolas Carlier
 */
@Named("pollService")
public class PollServiceImpl extends GenericServiceImpl<Poll, Long, PollRepository> implements PollService {

    private static final Logger LOG = LoggerFactory.getLogger(PollServiceImpl.class);

    @Value("#{config['rt.data.dir']}")
    private String dataDirPath;

    @Inject
    @Named("pollRepository")
    @Override
    public void setRepository(PollRepository pollRepository) {
        this.repository = pollRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public Poll create(final Poll resource) {
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

        // Illustration
        if (resource.getIllustration() != null && !"".equals(resource.getIllustration())) {
            poll.setIllustration(resource.getIllustration());
            String tmpdir = System.getProperty("java.io.tmpdir");

            String illustrationDir = new StringBuilder(dataDirPath).append(File.separator).append("illustration")
                    .toString();
            String illustrationLocation = new StringBuilder(illustrationDir).append(File.separator)
                    .append(resource.getIllustration()).toString();

            String tmpFileLocation = new StringBuilder(tmpdir).append(File.separator).append("rt_")
                    .append(resource.getIllustration()).append(".attachement").toString();
            try {
                FileTools.copy(tmpFileLocation, illustrationLocation);
                LOG.debug("{} saved.", illustrationLocation);
            } catch (IOException ex) {
                LOG.error("Unable to copy temp upload file to file repository", ex);
            }
        }

        // Set expiration date if null
        if (resource.getExpirationDate() == null) {
            date.add(Calendar.MONTH, 1);
            poll.setExpirationDate(date.getTime());
        } else {
            poll.setExpirationDate(resource.getExpirationDate());
        }

        return super.create(poll);
    }

    @Override
    public Page<Poll> find(final String query, final Pageable pageable) throws ServiceException {
        if (query == null || "".equals(query.trim())) {
            return this.findAll(pageable);
        }
        try {
            return this.repository.find(query, pageable);
        } catch (ParseException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    public void rebuildIndex() {
        this.repository.rebuildIndex();
    }

	@Override
	public Long getIdFromEntity(Poll poll) {
		return poll.getId();
	}
}

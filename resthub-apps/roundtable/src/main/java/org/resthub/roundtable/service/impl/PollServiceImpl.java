package org.resthub.roundtable.service.impl;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Named;
import org.apache.lucene.queryParser.ParseException;

import org.resthub.core.audit.annotation.Auditable;
import org.resthub.core.service.GenericResourceServiceImpl;
import org.resthub.roundtable.dao.PollDao;
import org.resthub.roundtable.model.Answer;
import org.resthub.roundtable.model.Poll;
import org.resthub.roundtable.service.PollService;
import org.resthub.roundtable.service.common.ServiceException;
import org.resthub.roundtable.toolkit.FileTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.synyx.hades.domain.Page;
import org.synyx.hades.domain.Pageable;

/**
 * Poll service implementation.
 * @author Nicolas Carlier
 */
@Named("pollService")
public class PollServiceImpl extends GenericResourceServiceImpl<Poll, PollDao> implements PollService {
    private static final Logger LOG = LoggerFactory.getLogger(PollServiceImpl.class);

    @Value("#{config['rt.data.dir']}")
    private String dataDirPath;

    @Inject
    @Named("pollDao")
    @Override
    public void setDao(PollDao pollDao) {
        this.dao = pollDao;
    }

    @Override
    @Auditable
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

	    String illustrationDir = new StringBuilder(dataDirPath).append(File.separator).append("illustration").toString();
	    String illustrationLocation = new StringBuilder(illustrationDir)
                    .append(File.separator).append(resource.getIllustration())
                    .toString();
	    
	    String tmpFileLocation = new StringBuilder(tmpdir)
                    .append(File.separator).append("rt_")
                    .append(resource.getIllustration())
                    .append(".attachement")
                    .toString();
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
        }
        else {
            poll.setExpirationDate(resource.getExpirationDate());
        }

        return super.create(poll);
    }

    @Override
    @Auditable
    public Page<Poll> find(final String query, final Pageable pageable) throws ServiceException {
        if (query == null || "".equals(query.trim())) {
            return this.findAll(pageable);
        }
        try {
            return this.dao.find(query, pageable);
        } catch (ParseException ex) {
            throw new ServiceException(ex.getMessage(), ex);
        }
    }

    @Override
    @Auditable
    public void rebuildIndex() {
        this.dao.rebuildIndex();
    }
}

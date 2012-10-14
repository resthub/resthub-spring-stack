package org.resthub.web.controller;

import javax.inject.Inject;

import org.resthub.web.model.Sample;
import org.resthub.web.repository.SampleResourceRepository;
import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/repository-based")
public class SampleRepositoryBasedRestController extends
        RepositoryBasedRestController<Sample, Long, SampleResourceRepository> {

    @Override
    @Inject
    public void setRepository(SampleResourceRepository repository) {
        this.repository = repository;
    }
    
}

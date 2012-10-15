package org.resthub.web.repository;

import org.resthub.web.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleResourceRepository extends JpaRepository<Sample, Long> {
    
    Sample findByName(String name);

}

package org.resthub.web.repository;

import org.resthub.web.model.Sample;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebSampleResourceRepository extends JpaRepository<Sample, Long> {

}

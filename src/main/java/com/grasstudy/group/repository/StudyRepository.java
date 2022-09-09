package com.grasstudy.group.repository;

import com.grasstudy.group.entity.Study;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends ReactiveCrudRepository<Study, Long> {

}
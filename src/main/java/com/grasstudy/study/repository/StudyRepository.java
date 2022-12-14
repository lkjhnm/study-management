package com.grasstudy.study.repository;

import com.grasstudy.study.entity.Study;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends ReactiveCrudRepository<Study, String> {

}

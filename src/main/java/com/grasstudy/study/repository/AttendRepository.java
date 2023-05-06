package com.grasstudy.study.repository;

import com.grasstudy.attend.entity.Attend;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendRepository extends ReactiveCrudRepository<Attend, String> {

}

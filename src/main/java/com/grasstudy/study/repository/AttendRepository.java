package com.grasstudy.study.repository;

import com.grasstudy.attend.entity.Attend;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AttendRepository extends ReactiveCrudRepository<Attend, String> {

}

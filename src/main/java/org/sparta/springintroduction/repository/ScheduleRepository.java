package org.sparta.springintroduction.repository;

import org.sparta.springintroduction.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}

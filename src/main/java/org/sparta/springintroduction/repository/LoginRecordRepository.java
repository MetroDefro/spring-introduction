package org.sparta.springintroduction.repository;

import org.sparta.springintroduction.entity.LoginRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRecordRepository extends JpaRepository<LoginRecord, Long> {
}

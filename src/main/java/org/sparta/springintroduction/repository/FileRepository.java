package org.sparta.springintroduction.repository;

import org.sparta.springintroduction.entity.File;
import org.springframework.data.repository.CrudRepository;

public interface FileRepository extends CrudRepository<File, Long> {
}

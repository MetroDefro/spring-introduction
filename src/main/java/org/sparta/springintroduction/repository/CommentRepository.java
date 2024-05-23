package org.sparta.springintroduction.repository;

import org.sparta.springintroduction.entity.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}

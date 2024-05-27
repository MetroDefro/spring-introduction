package org.sparta.springintroduction.service;

import org.sparta.springintroduction.dto.CommentRequestDto;
import org.sparta.springintroduction.dto.CommentResponseDto;
import org.sparta.springintroduction.entity.Comment;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.entity.User;
import org.sparta.springintroduction.repository.CommentRepository;
import org.sparta.springintroduction.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public CommentResponseDto createComment(Long sceduleId, CommentRequestDto requestDto, User user) {
        // dto로부터 새 엔티티를 생성할 때 schedule 주입한다.
        Schedule schedule = findScheduleById(sceduleId);
        Comment comment = requestDto.toEntity(findScheduleById(sceduleId), user.getUsername());
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    public CommentResponseDto updateComment(Long sceduleId, Long id, CommentRequestDto requestDto, User user) {
        findScheduleById(sceduleId);
        Comment comment = findCommentById(id);
        if(!Objects.equals(comment.getUsername(), user.getUsername())) {
            throw new SecurityException("작성자만 삭제/수정할 수 있습니다.");
        } else {
            return new CommentResponseDto(comment.update(requestDto.getContents()));
        }
    }

    public String deleteComment(Long sceduleId, Long id, User user) {
        findScheduleById(sceduleId);
        Comment comment = findCommentById(id);
        if(!Objects.equals(user.getUsername(), comment.getUsername())) {
            throw new SecurityException("작성자만 삭제/수정할 수 있습니다.");
        } else {
            commentRepository.delete(comment);
            return "댓글 삭제를 성공하였습니다.";
        }
    }

    private Schedule findScheduleById(Long id) {
        // 선택한 일정 DB에서 찾기. 없으면 예외 처리.
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));
    }

    private Comment findCommentById(Long id) {
        // 선택한 댓글 DB에서 찾기. 없으면 예외 처리.
        return commentRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
    }
}

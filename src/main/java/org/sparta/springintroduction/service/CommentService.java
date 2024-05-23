package org.sparta.springintroduction.service;

import org.sparta.springintroduction.dto.CommentRequestDto;
import org.sparta.springintroduction.dto.CommentResponseDto;
import org.sparta.springintroduction.entity.Comment;
import org.sparta.springintroduction.entity.Schedule;
import org.sparta.springintroduction.repository.CommentRepository;
import org.sparta.springintroduction.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public CommentService(CommentRepository commentRepository, ScheduleRepository scheduleRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public CommentResponseDto createComment(Long sceduleId, CommentRequestDto requestDto) {
        // dto로부터 새 엔티티를 생성할 때 schedule 주입한다.
        Comment comment = requestDto.toEntity(findScheduleById(sceduleId));
        Comment savedComment = commentRepository.save(comment);
        return new CommentResponseDto(savedComment);
    }

    private Schedule findScheduleById(Long id) {
        // 선택한 일정이 DB에서 찾기. 없으면 예외 처리.
        return scheduleRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 일정을 찾을 수 없습니다."));
    }
}

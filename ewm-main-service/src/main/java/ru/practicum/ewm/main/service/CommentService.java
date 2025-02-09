package ru.practicum.ewm.main.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.dto.CommentStatusUpdateDto;
import ru.practicum.ewm.main.dto.NewCommentDto;
import ru.practicum.ewm.main.entity.Comment;
import ru.practicum.ewm.main.entity.Event;
import ru.practicum.ewm.main.entity.User;
import ru.practicum.ewm.main.exception.IncorrectRequestException;
import ru.practicum.ewm.main.exception.NotFoundException;
import ru.practicum.ewm.main.exception.UserRequestException;
import ru.practicum.ewm.main.mapper.CommentMapper;
import ru.practicum.ewm.main.model.CommentStatus;
import ru.practicum.ewm.main.model.EventState;
import ru.practicum.ewm.main.repository.CommentFilter;
import ru.practicum.ewm.main.repository.CommentRepository;
import ru.practicum.ewm.main.repository.EventRepository;
import ru.practicum.ewm.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.main.model.CommentStatus.PENDING;
import static ru.practicum.ewm.main.model.CommentStatus.PUBLISHED;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User not found.");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event not found");
        });

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new UserRequestException("Event not published");
        }

        Comment comment = commentRepository.save(new Comment()
                .setUser(user)
                .setEvent(event)
                .setCreatedOn(LocalDateTime.now())
                .setStatus(PENDING)
                .setText(newCommentDto.getText()));

        return commentMapper.toDto(comment);
    }

    public CommentDto updateComment(Long userId, Long eventId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = commentRepository.findByIdAndUserIdAndEventId(commentId, userId, eventId).orElseThrow(() -> {
            throw new NotFoundException("Comment not found");
        });

        if (comment.getStatus().equals(PENDING)) {
            throw new UserRequestException("Unable to edit comment in this state");
        }

        comment.setText(newCommentDto.getText())
                .setCreatedOn(LocalDateTime.now())
                .setStatus(PENDING);

        commentRepository.save(comment);
        return commentMapper.toDto(comment);
    }

    public List<CommentDto> updateComment(CommentStatusUpdateDto updateRequest) {
        List<Comment> comments = commentRepository.findAllByIdInAndStatus(updateRequest.getCommentIds(), PENDING);

        if (comments.size() != updateRequest.getCommentIds().size()) {
            throw new NotFoundException("Incorrect comment id(s) in the request body.");
        }

        CommentStatus status = updateRequest.getStatus();
        switch (status) {
            case PUBLISHED -> {
                comments.forEach(comment -> comment.setStatus(PUBLISHED));
                comments = commentRepository.saveAll(comments);
                return comments.stream()
                        .map(commentMapper::toDto)
                        .toList();
            }
            case DELETED -> {
                commentRepository.deleteAllById(updateRequest.getCommentIds());
                return List.of();
            }
            case PENDING -> throw new IncorrectRequestException("Unable to update in pending status");
            default -> throw new IncorrectRequestException("Unknown status");
        }
    }

    public void deleteComment(Long userId, Long eventId, Long commentId) {
        commentRepository.findPublished(commentId, userId, eventId)
                .ifPresentOrElse(commentRepository::delete, () -> {
                    throw new NotFoundException("Comment for deletion not found");
                });
    }

    public CommentDto getComment(Long userId, Long eventId, Long commentId) {
        Comment comment = commentRepository.findByIdAndUserIdAndEventId(commentId, userId, eventId).orElseThrow(() -> {
            throw new NotFoundException("Comment not found");
        });

        if (comment.getStatus().equals(PENDING)) {
            throw new UserRequestException("Unable to view comment in this state");
        }
        return commentMapper.toDto(comment);
    }

    public List<CommentDto> getComments(Long userId) {
        List<Comment> comments = commentRepository.findAllByUserIdAndStatus(userId, PUBLISHED);
        if (!comments.isEmpty()) {
            return comments.stream()
                    .map(commentMapper::toDto)
                    .toList();
        }
        return List.of();
    }

    public List<CommentDto> getComments(CommentFilter commentFilter, Pageable pageable) {
        LocalDateTime rangeStart = commentFilter.rangeStart();
        LocalDateTime rangeEnd = commentFilter.rangeEnd();
        if ((rangeStart != null && rangeEnd != null) && (rangeStart.isAfter(rangeEnd) || rangeStart.isEqual(rangeEnd))) {
            throw new IncorrectRequestException("Start time must not after or equal to end time.");
        }
        Specification<Comment> commentSpecification = commentFilter.toSpecification();
        Page<Comment> comments = commentRepository.findAll(commentSpecification, pageable);

        return comments.map(commentMapper::toDto).getContent();
    }

    public List<CommentDto> getComments(Long userId, Long eventId) {
        List<Comment> comments = commentRepository.findAllByUserIdAndEventIdAndStatus(userId, eventId, PUBLISHED);
        if (!comments.isEmpty()) {
            return comments.stream()
                    .map(commentMapper::toDto)
                    .toList();
        }
        return List.of();
    }
}

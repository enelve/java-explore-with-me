package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.main.entity.Comment;
import ru.practicum.ewm.main.model.CommentStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

    Optional<Comment> findByIdAndUserIdAndEventId(Long commentId, Long userId, Long eventId);

    List<Comment> findAllByUserIdAndStatus(Long userId, CommentStatus published);

    List<Comment> findAllByUserIdAndEventIdAndStatus(Long userId, Long eventId, CommentStatus published);

    Optional<Comment> findByIdAndUserIdAndEventIdAndStatus(Long commentId, Long userId, Long eventId, CommentStatus commentStatus);

    default Optional<Comment> findPublished(Long commentId, Long userId, Long eventId) {
        return findByIdAndUserIdAndEventIdAndStatus(commentId, userId, eventId, CommentStatus.PUBLISHED);
    }

    List<Comment> findAllByIdInAndStatus(List<Long> commentIds, CommentStatus status);
}

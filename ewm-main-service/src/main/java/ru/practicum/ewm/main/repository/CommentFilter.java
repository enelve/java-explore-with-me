package ru.practicum.ewm.main.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.ewm.main.entity.Comment;
import ru.practicum.ewm.main.model.CommentStatus;

import java.time.LocalDateTime;
import java.util.List;

public record CommentFilter(
        String text,
        List<Long> users,
        List<CommentStatus> statuses,
        List<Long> events,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime rangeStart,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime rangeEnd) {

    public Specification<Comment> toSpecification() {
        return Specification.where(textSpec()
                .and(eventsSpec())
                .and(usersSpec())
                .and(statusesSpec())
                .and(rangeStartSpec())
                .and(rangeEndSpec())
        );

    }

    private Specification<Comment> textSpec() {
        return (root, query, cb) -> text != null ? cb.like(root.get("text"), "%" + text + "%") : null;
    }

    private Specification<Comment> eventsSpec() {
        return (root, query, cb) -> events != null ? root.get("event").get("id").in(events) : null;
    }

    private Specification<Comment> usersSpec() {
        return (root, query, cb) -> users != null ? root.get("user").get("id").in(users) : null;
    }

    private Specification<Comment> statusesSpec() {
        return (root, query, cb) -> statuses != null ? root.get("status").in(statuses) : null;
    }

    private Specification<Comment> rangeStartSpec() {
        return (root, query, cb) -> rangeStart != null
                ? cb.greaterThan(root.get("createdOn").as(LocalDateTime.class), rangeStart)
                : null;
    }

    private Specification<Comment> rangeEndSpec() {
        return (root, query, cb) -> rangeEnd != null
                ? cb.lessThan(root.get("createdOn").as(LocalDateTime.class), rangeEnd)
                : null;
    }
}

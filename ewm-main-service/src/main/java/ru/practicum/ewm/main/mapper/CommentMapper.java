package ru.practicum.ewm.main.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.main.dto.CommentDto;
import ru.practicum.ewm.main.entity.Comment;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final EventMapper eventMapper;

    public CommentDto toDto(Comment comment) {
        return new CommentDto()
                .setCreatedOn(comment.getCreatedOn())
                .setEvent(eventMapper.eventToShortDto(comment.getEvent()))
                .setUser(UserMapper.toUserShortDto(comment.getUser()))
                .setText(comment.getText());
    }
}

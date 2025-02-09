package ru.practicum.ewm.main.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.main.model.CommentStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentStatusUpdateDto {

    @NotNull
    @NotEmpty
    private List<Long> commentIds;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
}

package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private long id;
    private String authorName;
    @NotNull
    @NotEmpty
    private String text;
    private LocalDateTime created;
}

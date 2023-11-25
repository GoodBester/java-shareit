package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemRequestDto {
    private Long id;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
}

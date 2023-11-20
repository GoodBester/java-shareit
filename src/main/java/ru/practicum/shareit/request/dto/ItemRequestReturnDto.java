package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestReturnDto {

    private Long id;
    private long requesterId;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created;
    private List<ItemDtoForRequest> items;
}

package ru.practicum.shareit.request.dto;

import lombok.Data;


@Data
public class ItemDtoForRequest {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private long requestId;

}

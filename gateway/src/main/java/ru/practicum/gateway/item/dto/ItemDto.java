package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDto {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private long requestId;
}

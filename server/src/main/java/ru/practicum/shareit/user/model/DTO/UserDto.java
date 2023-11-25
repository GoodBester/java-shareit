package ru.practicum.shareit.user.model.DTO;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private long id;
    private String name;
    private String email;

}

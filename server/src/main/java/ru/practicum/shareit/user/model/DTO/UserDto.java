package ru.practicum.shareit.user.model.DTO;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private long id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    @NotNull
    private String email;

}

package ru.practicum.shareit.user.model.DTO;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;

}

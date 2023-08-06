package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    private List<Integer> itemsList = new ArrayList<>();

}

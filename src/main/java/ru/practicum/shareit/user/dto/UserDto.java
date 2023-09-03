package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private int id;
    @Size(min = 1, max = 300)
    @NotNull
    private String name;
    @Size(min = 1, max = 300)
    @Email
    @NotNull
    private String email;
}

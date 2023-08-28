package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    private final int id;
    @Size(min = 1, max = 15)
    @NotNull
    private final String name;
    @Email
    @NotNull
    private final String email;
}

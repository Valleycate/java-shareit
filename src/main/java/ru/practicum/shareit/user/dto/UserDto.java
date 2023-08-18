package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @Size(min = 1, max = 15)
    private final String name;
    @Email
    private final String email;
}

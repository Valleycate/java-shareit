package ru.practicum.shareit.request.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Setter
@Entity(name = "requests")
@Table(name = "requests")
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private int id;
    @ManyToOne()
    @NotNull
    private User requester;
    @NotNull
    @NotBlank
    @Column(name = "description", nullable = false, length = 300)
    private String description;
    @Column(name = "created", nullable = false)
    @NotNull
    private LocalDateTime created;
}

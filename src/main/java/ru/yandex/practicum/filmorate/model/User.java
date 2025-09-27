package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * User.
 */

@Data
@AllArgsConstructor
public class User {
    private Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;

    /*@ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )*/
    private Set<Long> friends;

    /*@ManyToMany
    @JoinTable(
            name = "friend_requests",
            joinColumns = @JoinColumn(name = "sender_id"),
            inverseJoinColumns = @JoinColumn(name = "receiver_id")
    )*/
    private Set<Long> sendFriendRequests;

    /*@ManyToMany
    @JoinTable(
            name = "friend_requests",
            joinColumns = @JoinColumn(name = "receiver_id"),
            inverseJoinColumns = @JoinColumn(name = "sender_id")
    )*/
    private Set<Long> receiveFriendRequests;
}
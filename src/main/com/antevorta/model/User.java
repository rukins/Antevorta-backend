package com.antevorta.model;

import com.antevorta.exception.serverexception.IncorrectEmailException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String firstname;
    private String lastname;

    public User(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    @SneakyThrows
    public void setEmail(String email) {
        if (email == null || !isEmailCorrect(email)) {
            throw new IncorrectEmailException("Incorrect email");
        }

        this.email = email;
    }

    private Boolean isEmailCorrect(String email) {
        return Pattern.compile("^[\\w-.]+@[\\w-]+\\.[\\w-]+$").matcher(email).matches();
    }
}

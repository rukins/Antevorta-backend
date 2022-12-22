package com.antevorta.model;

import com.antevorta.exception.serverexception.IncorrectEmailException;
import com.antevorta.exception.serverexception.MissedValueException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.util.regex.Pattern;

@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Column(unique = true)
    private String email;

    @Getter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Getter
    private String firstname;

    @Getter
    private String lastname;

    @Getter
    @Setter
    @JsonProperty(value = "verified", access = JsonProperty.Access.READ_ONLY)
    private boolean isVerified;

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

    @SneakyThrows
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new MissedValueException("Password can't be empty");
        }

        this.password = password;
    }

    @SneakyThrows
    public void setFirstname(String firstname) {
        if (firstname == null || firstname.isEmpty()) {
            throw new MissedValueException("Firstname can't be empty");
        }

        this.firstname = firstname;
    }

    @SneakyThrows
    public void setLastname(String lastname) {
        if (lastname == null || lastname.isEmpty()) {
            throw new MissedValueException("Lastname can't be empty");
        }

        this.lastname = lastname;
    }

    private Boolean isEmailCorrect(String email) {
        return Pattern.compile("^[\\w-.]+@[\\w-]+\\.[\\w-]+$").matcher(email).matches();
    }
}

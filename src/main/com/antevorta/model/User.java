package com.antevorta.model;

import com.antevorta.exception.serverexception.IncorrectEmailException;
import com.antevorta.exception.serverexception.MissedValueException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.regex.Pattern;

@JsonPropertyOrder({ "id", "email", "password", "firstname", "lastname", "verified", "authorities" })
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

    @Getter
    @Setter
    @JsonProperty(value = "authorities", access = JsonProperty.Access.READ_ONLY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    public User(String email, String password, String firstname, String lastname) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public User(String email, String password, String firstname, String lastname, Set<Authority> authorities) {
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.authorities = authorities;
    }

    public void setEmail(String email) {
        if (email == null || !isEmailCorrect(email)) {
            throw new IncorrectEmailException("Incorrect email");
        }

        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new MissedValueException("Password can't be empty");
        }

        this.password = password;
    }

    public void setFirstname(String firstname) {
        if (firstname == null || firstname.isEmpty()) {
            throw new MissedValueException("Firstname can't be empty");
        }

        this.firstname = firstname;
    }

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

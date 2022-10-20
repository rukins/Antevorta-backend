package com.inventorymanagementsystem.model;

import com.inventorymanagementsystem.exception.globalexception.IncorrectEmailException;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.regex.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    private String email;
    private String password;
    private String firstname;
    private String lastname;

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

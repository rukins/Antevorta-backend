package com.inventorymanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Boolean hasValidEmail() {
        return Pattern.compile("^[\\w-.]+@[\\w-]+\\.[\\w-]+$").matcher(this.email).matches();
    }
}

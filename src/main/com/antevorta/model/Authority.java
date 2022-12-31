package com.antevorta.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @Column(unique = true, updatable = false, nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "authorities")
    private Set<User> users;

    public Authority(String name) {
        this.name = name;
    }
}

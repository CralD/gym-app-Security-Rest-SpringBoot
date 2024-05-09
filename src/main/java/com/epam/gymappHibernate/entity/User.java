package com.epam.gymappHibernate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {

    private String firstName;
    private String lastName;
    private String userName;
    private String password;
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private boolean isActive;

    public User(String firstName, String lastName, String userName, String password, Long id, boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
        this.id = id;
        this.isActive = isActive;
    }


}

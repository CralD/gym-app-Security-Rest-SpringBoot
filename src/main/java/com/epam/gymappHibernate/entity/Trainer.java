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
public class Trainer extends User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long trainerId;
    private String specialization;

    public Trainer(String firstName, String lastName, String userName, String password, Long id, boolean isActive, String specialization) {
        super(firstName, lastName, userName, password, id, isActive);
        this.specialization = specialization;
    }


}

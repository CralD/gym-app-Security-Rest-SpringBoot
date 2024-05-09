package com.epam.gymappHibernate.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

    @Entity
    @Getter
    @Setter
    public class Trainee extends User {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long traineeID ;

        private LocalDate DateOfBirth;
        private String Address;

        public Trainee(String firstName,String lastName,String password, String userName,Long id,boolean isActive,LocalDate dateOfBirth, String address) {
            super(firstName,lastName,userName,password,id,isActive);
            DateOfBirth = dateOfBirth;
            Address = address;
        }


    }


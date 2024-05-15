package com.epam.gymappHibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "TRAINEE")
@Getter
@Setter
public class Trainee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long traineeID;
    @Column(name = "DATEOFBIRTH")
    private LocalDate DateOfBirth;
    @Column(name = "ADDRESS")
    private String Address;
    @OneToOne
    @JoinColumn(name = "USERID", referencedColumnName = "ID")
    private User user;
    @OneToMany(mappedBy = "trainee")
    private List<Training> trainings;
    @ManyToMany
    @JoinTable(
            name = "TRAINEE_TRAINER",
            joinColumns = @JoinColumn(name = "TRAINEE_ID"),
            inverseJoinColumns = @JoinColumn(name = "TRAINER_ID")
    )
    private Set<Trainer> trainers;

    public Trainee() {
    }
}


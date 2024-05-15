package com.epam.gymappHibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Entity
@Table(name = "TRAINING")
@Getter
@Setter
public class Training {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long trainingId;
    @Column(name = "TRAININGNAME", nullable = false)
    private String trainingName;
    @Column(name = "TRAININGTYPEID")
    private String trainingType;
    @Column(name = "TRAININGDATE", nullable = false)
    private LocalDate trainingDate;
    @Column(name = "TRAININGDURATION", nullable = false)
    private  int trainingDuration;

    @ManyToOne
    @JoinColumn(name = "TRAINEEID", referencedColumnName = "ID")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "TRAINERID", referencedColumnName = "ID")
    private Trainer trainer;

    public Training() {

    }

    public Training(String trainingName, String trainingType, LocalDate trainingDate, int trainingDuration, Trainee trainee, Trainer trainer) {
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainee = trainee;
        this.trainer = trainer;
    }




}

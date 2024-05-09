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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long trainingId;
    private String trainingName;
    private String trainingType;
    private LocalDate trainingDate;
    private  int trainingDuration;

    private Long trainee;

    private Long trainer;


    public Training(String trainingName, String trainingType, LocalDate trainingDate, int trainingDuration, Trainee trainee, Trainer trainer) {
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainingDuration = trainingDuration;
        this.trainee = trainee.getTraineeID();
        this.trainer = trainer.getTrainerId();
    }




}

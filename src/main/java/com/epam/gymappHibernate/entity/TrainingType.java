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
public class TrainingType {




    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long trainingTypeId;
    private String trainingTypeName;

    public TrainingType(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }


}

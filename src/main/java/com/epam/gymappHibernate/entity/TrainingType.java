package com.epam.gymappHibernate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "TRAININGTYPE")
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

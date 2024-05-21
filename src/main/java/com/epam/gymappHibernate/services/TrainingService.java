package com.epam.gymappHibernate.services;


import com.epam.gymappHibernate.dao.TrainingRepository;
import com.epam.gymappHibernate.entity.Training;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrainingService {
    @Autowired
    private TrainingRepository trainingRepository;

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public void saveTraining(Training training) {
        trainingRepository.saveTraining(training);
        logger.info("Training saved: {}", training.getTrainingId());
    }

    public List<Training> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName, String trainingType) {
        logger.info("Fetching trainings for trainee: {}, fromDate: {}, toDate: {}, trainerName: {}, trainingType: {}",
                username, fromDate, toDate, trainerName, trainingType);
        return trainingRepository.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
    }

    public List<Training> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        logger.info("Fetching trainings for trainer: {}, fromDate: {}, toDate: {}, traineeName: {}",
                username, fromDate, toDate, traineeName);
        return trainingRepository.getTrainerTrainings(username, fromDate, toDate, traineeName);
    }
}

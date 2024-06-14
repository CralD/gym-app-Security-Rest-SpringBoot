package com.epam.gymappHibernate.services;


import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dao.TrainingRepository;
import com.epam.gymappHibernate.dao.TrainingTypeRepository;
import com.epam.gymappHibernate.dto.TrainingDto;
import com.epam.gymappHibernate.dto.TrainingDtoResponse;
import com.epam.gymappHibernate.dto.TrainingTypeDto;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.Training;
import com.epam.gymappHibernate.entity.TrainingType;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingService {
    @Autowired
    private TrainingRepository trainingRepository;
    private TraineeRepository traineeRepository;
    private TrainerRepository trainerRepository;
    private TrainingTypeRepository trainingTypeRepository;

    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, TrainerRepository trainerRepository, TraineeRepository traineeRepository,TrainingTypeRepository trainingTypeRepository) {
        this.trainingRepository = trainingRepository;
        this.traineeRepository = traineeRepository;
        this.trainerRepository = trainerRepository;
        this.trainingTypeRepository = trainingTypeRepository;
    }

    @Transactional
    public void saveTraining(Training training) {
        trainingRepository.saveTraining(training);
        logger.info("Training saved: {}", training.getTrainingId());
    }
    @Transactional
    public void addTraining(String Traineeusername, String Trainerusername,TrainingDto request) {

        Trainee trainee = traineeRepository.getTraineeByUsername(Traineeusername);

        Trainer trainer = trainerRepository.getTrainerByUsername(Trainerusername);

        Training training = new Training();
        training.setTrainingName(request.getTrainingName());
        training.setTrainingDate(request.getTrainingDate());
        training.setTrainingDuration(request.getTrainingDuration());
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        trainingRepository.saveTraining(training);

        logger.info("Training added: {}", training.getTrainingName());
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
    public List<TrainingTypeDto> getAllTrainingTypes() {
        List<TrainingType> trainingTypes = trainingTypeRepository.findAll();
        return trainingTypes.stream().map(trainingType -> {
            TrainingTypeDto dto = new TrainingTypeDto();
            dto.setId(trainingType.getTrainingTypeId());
            dto.setTrainingTypeName(trainingType.getTrainingTypeName());
            return dto;
        }).collect(Collectors.toList());
    }

    public TrainingDtoResponse convertToDto(Training training) {
        TrainingDtoResponse dto = new TrainingDtoResponse();
        dto.setTrainingName(training.getTrainingName());
        dto.setTrainingDate(training.getTrainingDate());
        dto.setTrainingType(training.getTrainingType().getTrainingTypeName());
        dto.setTrainingDuration(training.getTrainingDuration());
        dto.setTrainerName(training.getTrainer().getUser().getUserName());
        return dto;
    }
}

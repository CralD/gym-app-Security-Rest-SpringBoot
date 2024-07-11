package com.epam.gymappHibernate.services;


import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.dto.CredentialsDto;
import com.epam.gymappHibernate.dto.TraineeDto;
import com.epam.gymappHibernate.dto.TrainerDto;
import com.epam.gymappHibernate.dto.UserDto;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.User;
import com.epam.gymappHibernate.exception.NoTrainingsFoundException;
import com.epam.gymappHibernate.util.PasswordGenerator;
import com.epam.gymappHibernate.util.UsernameGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TraineeService {
    @Autowired
    private final TraineeRepository traineeRepository;
    @Autowired
    private final UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    public TraineeService(TraineeRepository traineeRepository, UserRepository userRepository) {
        this.traineeRepository = traineeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CredentialsDto createTrainee(TraineeDto request) {
        Trainee trainee = mapToTrainee(request);

        List<String> existingUsernames = userRepository.getAllUsers();
        String username = UsernameGenerator.generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName(), existingUsernames);
        trainee.getUser().setUserName(username);
        String password = PasswordGenerator.generatePassword();
        String encodedPassword = passwordEncoder.encode(password);
        trainee.getUser().setPassword(encodedPassword);
        trainee.getUser().setActive(true);
        User user = trainee.getUser();
        userRepository.saveUser(user);
        traineeRepository.saveTrainee(trainee);
        logger.info("Trainee created: {} ", username);
        return new CredentialsDto(username,password);
    }

    public boolean authenticate(String username, String password) {
        Trainee trainee = traineeRepository.getTraineeByUsername(username);
        if (trainee != null && trainee.getUser().getPassword().equals(password)) {
            logger.info("Authentication successful for user: {}", username);
            return true;
        } else {
            logger.warn("Authentication failed for user: {}", username);
            return false;
        }
    }

    @Transactional
    public void deleteTrainee(String username) {

            traineeRepository.deleteTraineeByUsername(username);
            logger.info("Deleted trainee: {}", username);


    }


    public Trainee getTraineeByUsername(String username ) {

            logger.info("Selecting Trainee profile: {}", username);
            Trainee trainee = traineeRepository.getTraineeByUsername(username);
            if (trainee == null) {
                throw new NoTrainingsFoundException("Trainee not found");
            }
            return trainee;



    }

    @Transactional
    public Trainee updateTraineeProfile(String username, TraineeDto traineeDto) {

            logger.info("Updating Trainee profile: {}", username);

            Trainee trainee = getTraineeByUsername(username);

            trainee.getUser().setFirstName(traineeDto.getFirstName());
            trainee.getUser().setLastName(traineeDto.getLastName());
            trainee.getUser().setActive(traineeDto.isActive());
            if (traineeDto.getDateOfBirth() != null) {
                trainee.setDateOfBirth(traineeDto.getDateOfBirth());
            }
            if (traineeDto.getAddress() != null) {
                trainee.setAddress(traineeDto.getAddress());
            }
            traineeRepository.updateTrainee(trainee);

            return trainee;

    }

    @Transactional
    public void changeTraineePassword(String username, String newPassword, String password) {
        if (authenticate(username, password)) {
            Trainee trainee = traineeRepository.getTraineeByUsername(username);
            if (trainee != null) {
                logger.info("Changing Password");
                trainee.getUser().setPassword(newPassword);
                traineeRepository.updateTrainee(trainee);
            }
        } else {
            logger.error("Invalid username or password for trainee {}", username);
            throw new SecurityException("Invalid username or password");
        }
    }

    @Transactional
    public void setTraineeActiveStatus(String username, boolean isActive) {
        logger.info("Setting active status for trainee: {}", username);

            Trainee trainee = traineeRepository.getTraineeByUsername(username);
            if (trainee != null) {
                trainee.getUser().setActive(isActive);
                traineeRepository.updateTrainee(trainee);
                logger.info("Active status for trainee {} set to {}", username, isActive);
            } else {
                logger.warn("Trainee {} not found", username);
            }

    }

    public Trainee mapToTrainee(TraineeDto traineeDto) {
        User user = new User();
        user.setFirstName(traineeDto.getFirstName());
        user.setLastName(traineeDto.getLastName());

        Trainee trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(traineeDto.getDateOfBirth());
        trainee.setAddress(traineeDto.getAddress());

        return trainee;
    }
    public TraineeDto convertToTraineeDto(Trainee trainee) {
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName(trainee.getUser().getFirstName());
        traineeDto.setLastName(trainee.getUser().getLastName());
        traineeDto.setAddress(trainee.getAddress());
        traineeDto.setDateOfBirth(trainee.getDateOfBirth());
        traineeDto.setActive(trainee.getUser().isActive());

        List<TrainerDto> trainers = trainee.getTrainers().stream().map(trainer -> {
            TrainerDto trainerDto = new TrainerDto();
            trainerDto.setUserName(trainer.getUser().getUserName());
            trainerDto.setFirstName(trainer.getUser().getFirstName());
            trainerDto.setLastName(trainer.getUser().getLastName());
            if (trainer.getSpecialization() != null) {
                trainerDto.setSpecialization(trainer.getSpecialization().getTrainingTypeName());
            } else {
                trainerDto.setSpecialization(null);
            }
            return trainerDto;
        }).collect(Collectors.toList());

        traineeDto.setTrainers(trainers);
        return traineeDto;
    }

}

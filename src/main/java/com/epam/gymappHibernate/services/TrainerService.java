package com.epam.gymappHibernate.services;

import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.util.PasswordGenerator;
import com.epam.gymappHibernate.util.UsernameGenerator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainerService {
    @Autowired
    private final TrainerRepository trainerRepository;
    @Autowired
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);

    @Autowired
    public TrainerService(TrainerRepository trainerRepository, UserRepository userRepository) {
        this.trainerRepository = trainerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createTrainer(Trainer trainer) {

        List<String> existingUsernames = userRepository.getAllUsers();
        String username = UsernameGenerator.generateUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName(), existingUsernames);
        trainer.getUser().setUserName(username);
        String password = PasswordGenerator.generatePassword();
        trainer.getUser().setPassword(password);
        trainerRepository.saveTrainer(trainer);
        logger.info("Trainer created: {} ", username);
    }

    public boolean authenticate(String username, String password) {
        Trainer trainer = trainerRepository.getTrainerByUsername(username);
        if (trainer != null && trainer.getUser().getPassword().equals(password)) {
            logger.info("Authentication successful for user: {}", username);
            return true;
        } else {
            logger.warn("Authentication failed for user: {}", username);
            return false;
        }
    }

    public Trainer getTrainerByUsername(String username, String password) {
        if (authenticate(username, password)) {
            logger.info("Selecting Trainer profile: {}", username);
            return trainerRepository.getTrainerByUsername(username);

        } else {
            logger.error("Invalid username or password for trainer {}", username);
            throw new SecurityException("Invalid username or password");
        }
    }

    @Transactional
    public void updateTrainerProfile(String username, String password, Trainer trainer) {
        if (authenticate(username, password)) {
            logger.info("Updating Trainer profile: {}", username);
            trainerRepository.updateTrainer(trainer);
        } else {
            logger.error("Invalid username or password for trainer {}", username);
            throw new SecurityException("Invalid username or password");
        }
    }

    @Transactional
    public void changeTrainerPassword(String username, String newPassword, String password) {
        if (authenticate(username, password)) {
            Trainer trainer = trainerRepository.getTrainerByUsername(username);
            if (trainer != null) {
                logger.info("Changing Password");
                trainer.getUser().setPassword(newPassword);
                trainerRepository.updateTrainer(trainer);
            }
        } else {
            logger.error("Invalid username or password for trainer {}", username);
            throw new SecurityException("Invalid username or password");
        }
    }

    @Transactional
    public void setTrainerActiveStatus(String username, String password, boolean isActive) {
        logger.info("Setting active status for trainer: {}", username);
        if (authenticate(username, password)) {
            Trainer trainer = trainerRepository.getTrainerByUsername(username);
            if (trainer != null) {
                trainer.getUser().setActive(isActive);
                trainerRepository.updateTrainer(trainer);
                logger.info("Active status for trainer {} set to {}", username, isActive);
            } else {
                logger.warn("Trainer {} not found", username);
            }
        } else {
            logger.error("Invalid username or password for trainer {}", username);
            throw new SecurityException("Invalid username or password");
        }
    }

    public List<Trainer> findUnassignedTrainers(String traineeUsername) {
        logger.info("Fetching unassigned trainers for trainee: {}", traineeUsername);
        return trainerRepository.findUnassignedTrainers(traineeUsername);
    }


}

package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.Training;
import com.epam.gymappHibernate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb4")
class TrainingRepositoryTest {
    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    public void testSaveTraining() {
        // Create and save a new training
        Training training = new Training();
        training.setTrainingName("cardio");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60);

        // Persist the training
        trainingRepository.saveTraining(training);

        // Flush and clear the persistence context to force a database read
        entityManager.flush();
        entityManager.clear();

        // Retrieve the saved training
        Training savedTraining = entityManager.find(Training.class, training.getTrainingId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String expectedDate = sdf.format(training.getTrainingDate());
        String actualDate = sdf.format(savedTraining.getTrainingDate());

        // Verify the training was saved correctly
        assertNotNull(savedTraining);
        assertEquals("cardio", savedTraining.getTrainingName());
        assertEquals(60, savedTraining.getTrainingDuration());
        assertEquals(expectedDate,actualDate);
    }

    @Test
    @Transactional
    public void testSaveAndFindTrainings() {
        // Create and save a new user
        User user = new User();
        user.setFirstName("john");
        user.setLastName("doe");
        user.setPassword("abc123");
        user.setActive(true);
        user.setUserName("john.doe");
        userRepository.saveUser(user);

        // Create and save a new trainer
        Trainer trainer = new Trainer();
        trainer.setUser(user);
        trainerRepository.saveTrainer(trainer);

        User user2 = new User();
        user2.setFirstName("Paolo");
        user2.setLastName("Guerrero");
        user2.setPassword("awwww1444");
        user2.setActive(true);
        user2.setUserName("Paolo.Guerrero");
        userRepository.saveUser(user2);

        Trainee trainee = new Trainee();
        trainee.setUser(user2);
        traineeRepository.saveTrainee(trainee);


        // Create and save a new training
        Training training = new Training();
        training.setTrainer(trainer);
        training.setTrainingName("cardio");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60);
        training.setTrainee(trainee);
        trainingRepository.saveTraining(training);

        System.out.println("aca veamos si hay algo" +trainingRepository.getTrainerTrainings("john.doe", new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), new Date(), "Paolo"));
        // Clear the persistence context to force a database call when we try to fetch the Training again
        entityManager.flush();
        entityManager.clear();


        // Fetch the saved training from the database
        List<Training> savedTrainings = trainingRepository.getTrainerTrainings("john.doe", new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24), new Date(), "Paolo");

        System.out.println("Trainer username: " + trainer.getUser().getUserName());
        System.out.println("Trainee first name: " + trainee.getUser().getFirstName());
        System.out.println("date " + training.getTrainingDate());
        System.out.println("duration " + training.getTrainingDuration());

        System.out.println("Number of Trainings found: " + savedTrainings.size());
        // Assert that the training was saved correctly
        assertNotNull(savedTrainings);
        //assertFalse(savedTrainings.isEmpty()); // Ensure there is at least one training
        Training savedTraining = savedTrainings.get(0);
        assertEquals("john.doe", savedTraining.getTrainer().getUser().getUserName());
        assertEquals("Paolo.Guerrero", savedTraining.getTrainee().getUser().getUserName());
    }

}
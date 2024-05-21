package com.epam.gymappHibernate.services;

import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb6")
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TrainerService trainerService;

    private Trainer trainer;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setFirstName("Pedro");
        user.setLastName("Garcia");
        user.setUserName("Pedro.Garcia");
        user.setPassword("password");

        trainer = new Trainer();
        trainer.setUser(user);
    }

    @Test
    public void testCreateTrainer() {
        when(userRepository.getAllUsers()).thenReturn(Collections.singletonList("existingUser"));
        trainerService.createTrainer(trainer);
        assertNotNull(trainer.getUser().getUserName());
        assertNotNull(trainer.getUser().getPassword());
        verify(trainerRepository, times(1)).saveTrainer(trainer);
    }

    @Test
    public void testAuthenticateSuccess() {
        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        boolean result = trainerService.authenticate(username, password);
        assertTrue(result);
    }

    @Test
    public void testAuthenticateFailure() {
        String username = "johndoe";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");
        boolean result = trainerService.authenticate(username, password);
        assertFalse(result);
    }

    @Test
    public void testAuthenticateUserNotFound() {
        String username = "unknown";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(null);
        boolean result = trainerService.authenticate(username, password);
        assertFalse(result);
    }

    @Test
    public void testGetTrainerByUsernameSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        Trainer result = trainerService.getTrainerByUsername(username, password);
        assertEquals(trainer, result);
    }

    @Test
    public void testGetTrainerByUsernameFailure() {

        String username = "johndoe";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            trainerService.getTrainerByUsername(username, password);
        });
    }

    @Test
    public void testUpdateTrainerProfileSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        trainerService.updateTrainerProfile(username, password, trainer);
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }

    @Test
    public void testUpdateTrainerProfileFailure() {

        String username = "johndoe";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            trainerService.updateTrainerProfile(username, password, trainer);
        });
    }

    @Test
    public void testChangeTrainerPasswordSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        String newPassword = "newpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        trainerService.changeTrainerPassword(username, newPassword, password);
        assertEquals(newPassword, trainer.getUser().getPassword());
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }

    @Test
    public void testChangeTrainerPasswordFailure() {

        String username = "johndoe";
        String newPassword = "newpassword";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            trainerService.changeTrainerPassword(username, newPassword, password);
        });
    }

    @Test
    public void testSetTrainerActiveStatusSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        boolean isActive = true;
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);
        trainerService.setTrainerActiveStatus(username, password, isActive);
        assertEquals(isActive, trainer.getUser().isActive());
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }

    @Test
    public void testSetTrainerActiveStatusFailure() {

        String username = "johndoe";
        String password = "wrongpassword";
        boolean isActive = true;
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            trainerService.setTrainerActiveStatus(username, password, isActive);
        });
    }

    @Test
    public void testFindUnassignedTrainers() {

        String traineeUsername = "trainee1";
        List<Trainer> unassignedTrainers = Collections.singletonList(trainer);
        when(trainerRepository.findUnassignedTrainers(traineeUsername)).thenReturn(unassignedTrainers);
        List<Trainer> result = trainerService.findUnassignedTrainers(traineeUsername);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(trainer, result.get(0));
        verify(trainerRepository, times(1)).findUnassignedTrainers(traineeUsername);
    }

}
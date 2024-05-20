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
        // Arrange
        when(userRepository.getAllUsers()).thenReturn(Collections.singletonList("existingUser"));

        // Act
        trainerService.createTrainer(trainer);

        // Assert
        assertNotNull(trainer.getUser().getUserName());
        assertNotNull(trainer.getUser().getPassword());
        verify(trainerRepository, times(1)).saveTrainer(trainer);
    }
    @Test
    public void testAuthenticateSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);

        // Act
        boolean result = trainerService.authenticate(username, password);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAuthenticateFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");

        // Act
        boolean result = trainerService.authenticate(username, password);

        // Assert
        assertFalse(result);
    }
    @Test
    public void testAuthenticateUserNotFound() {
        // Arrange
        String username = "unknown";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(null);

        // Act
        boolean result = trainerService.authenticate(username, password);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetTrainerByUsernameSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);

        // Act
        Trainer result = trainerService.getTrainerByUsername(username, password);

        // Assert
        assertEquals(trainer, result);
    }

    @Test
    public void testGetTrainerByUsernameFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            trainerService.getTrainerByUsername(username, password);
        });
    }
    @Test
    public void testUpdateTrainerProfileSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);

        // Act
        trainerService.updateTrainerProfile(username, password, trainer);

        // Assert
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }

    @Test
    public void testUpdateTrainerProfileFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            trainerService.updateTrainerProfile(username, password, trainer);
        });
    }

    @Test
    public void testChangeTrainerPasswordSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        String newPassword = "newpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);

        // Act
        trainerService.changeTrainerPassword(username, newPassword, password);

        // Assert
        assertEquals(newPassword, trainer.getUser().getPassword());
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }

    @Test
    public void testChangeTrainerPasswordFailure() {
        // Arrange
        String username = "johndoe";
        String newPassword = "newpassword";
        String password = "wrongpassword";
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            trainerService.changeTrainerPassword(username, newPassword, password);
        });
    }
    @Test
    public void testSetTrainerActiveStatusSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        boolean isActive = true;
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword(password);

        // Act
        trainerService.setTrainerActiveStatus(username, password, isActive);

        // Assert
        assertEquals(isActive, trainer.getUser().isActive());
        verify(trainerRepository, times(1)).updateTrainer(trainer);
    }

    @Test
    public void testSetTrainerActiveStatusFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        boolean isActive = true;
        when(trainerRepository.getTrainerByUsername(username)).thenReturn(trainer);
        trainer.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            trainerService.setTrainerActiveStatus(username, password, isActive);
        });
    }

}
package com.epam.gymappHibernate.services;

import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:testdb5")
class TraineeServiceTest {

    @Mock
    private TraineeRepository traineeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setFirstName("Pedro");
        user.setLastName("Garcia");
        user.setUserName("Pedro.Garcia");
        user.setPassword("password");

        trainee = new Trainee();
        trainee.setUser(user);
    }

    @Test
    public void testCreateTrainee() {
        // Arrange
        when(userRepository.getAllUsers()).thenReturn(Collections.singletonList("existingUser"));

        // Act
        traineeService.createTrainee(trainee);

        // Assert
        assertNotNull(trainee.getUser().getUserName());
        assertNotNull(trainee.getUser().getPassword());
        verify(traineeRepository, times(1)).saveTrainee(trainee);
    }

    @Test
    public void testAuthenticateSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);

        // Act
        boolean result = traineeService.authenticate(username, password);

        // Assert
        assertTrue(result);
    }
    @Test
    public void testAuthenticateFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");

        // Act
        boolean result = traineeService.authenticate(username, password);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAuthenticateUserNotFound() {
        // Arrange
        String username = "unknown";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(null);

        // Act
        boolean result = traineeService.authenticate(username, password);

        // Assert
        assertFalse(result);
    }
    @Test
    public void testDeleteTraineeSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);

        // Act
        traineeService.deleteTrainee(username, password);

        // Assert
        verify(traineeRepository, times(1)).deleteTraineeByUsername(username);
    }

    @Test
    public void testDeleteTraineeFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            traineeService.deleteTrainee(username, password);
        });
    }

    @Test
    public void testGetTraineeByUsernameSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);

        // Act
        Trainee result = traineeService.getTraineeByUsername(username, password);

        // Assert
        assertEquals(trainee, result);
    }

    @Test
    public void testGetTraineeByUsernameFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            traineeService.getTraineeByUsername(username, password);
        });
    }
    @Test
    public void testUpdateTraineeProfileSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);

        // Act
        traineeService.updateTraineeProfile(username, password, trainee);

        // Assert
        verify(traineeRepository, times(1)).updateTrainee(trainee);
    }

    @Test
    public void testUpdateTraineeProfileFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            traineeService.updateTraineeProfile(username, password, trainee);
        });
    }
    @Test
    public void testChangeTraineePasswordSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        String newPassword = "newpassword";

        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);

        // Act
        traineeService.changeTraineePassword(username, newPassword, password);

        // Assert
        assertEquals(newPassword, trainee.getUser().getPassword());
        verify(traineeRepository, times(1)).updateTrainee(trainee);
    }

    @Test
    public void testChangeTraineePasswordFailure() {
        // Arrange
        String username = "johndoe";
        String newPassword = "newpassword";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            traineeService.changeTraineePassword(username, newPassword, password);
        });
    }
    @Test
    public void testSetTraineeActiveStatusSuccess() {
        // Arrange
        String username = "Pedro.Garcia";
        String password = "password";
        boolean isActive = true;
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);

        // Act
        traineeService.setTraineeActiveStatus(username, password, isActive);

        // Assert
        assertEquals(isActive, trainee.getUser().isActive());
        verify(traineeRepository, times(1)).updateTrainee(trainee);
    }

    @Test
    public void testSetTraineeActiveStatusFailure() {
        // Arrange
        String username = "johndoe";
        String password = "wrongpassword";
        boolean isActive = true;
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");

        // Act & Assert
        assertThrows(SecurityException.class, () -> {
            traineeService.setTraineeActiveStatus(username, password, isActive);
        });
    }

}
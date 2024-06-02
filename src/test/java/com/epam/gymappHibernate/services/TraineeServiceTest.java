package com.epam.gymappHibernate.services;

import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.UserRepository;
import com.epam.gymappHibernate.dto.TraineeDto;
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
import java.util.Date;

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

    private TraineeDto traineeDto;
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
        when(userRepository.getAllUsers()).thenReturn(Collections.singletonList("existingUser"));
        traineeService.createTrainee(trainee);
        assertNotNull(trainee.getUser().getUserName());
        assertNotNull(trainee.getUser().getPassword());
        verify(traineeRepository, times(1)).saveTrainee(trainee);
    }

    @Test
    public void testAuthenticateSuccess() {
        String username = "Pedro.Garcia";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        boolean result = traineeService.authenticate(username, password);
        assertTrue(result);
    }

    @Test
    public void testAuthenticateFailure() {
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");
        boolean result = traineeService.authenticate(username, password);
        assertFalse(result);
    }

    @Test
    public void testAuthenticateUserNotFound() {
        String username = "unknown";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(null);
        boolean result = traineeService.authenticate(username, password);
        assertFalse(result);
    }

    @Test
    public void testDeleteTraineeSuccess() {
        String username = "Pedro.Garcia";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);
        traineeService.deleteTrainee(username, password);
        verify(traineeRepository, times(1)).deleteTraineeByUsername(username);
    }

    @Test
    public void testDeleteTraineeFailure() {
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            traineeService.deleteTrainee(username, password);
        });
    }

    @Test
    public void testGetTraineeByUsernameSuccess() {
        String username = "Pedro.Garcia";
        String password = "password";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);
        Trainee result = traineeService.getTraineeByUsername(username, password);
        assertEquals(trainee, result);
    }

    @Test
    public void testGetTraineeByUsernameFailure() {
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            traineeService.getTraineeByUsername(username, password);
        });
    }

    @Test
    public void testUpdateTraineeProfileSuccess() {

        String username = "Pedro.Garcia";
        String password = "password";
        TraineeDto traineeDto = new TraineeDto();
        traineeDto.setFirstName("John");
        traineeDto.setLastName("Doe");
        traineeDto.setDateOfBirth(new Date());
        traineeDto.setAddress("123 Main St");
        traineeDto.setActive(true);
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);
        traineeService.updateTraineeProfile(username, password, traineeDto);
        verify(traineeRepository, times(1)).updateTrainee(trainee);
    }

    @Test
    public void testUpdateTraineeProfileFailure() {
        String username = "johndoe";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            traineeService.updateTraineeProfile(username, password, traineeDto);
        });
    }

    @Test
    public void testChangeTraineePasswordSuccess() {
        String username = "Pedro.Garcia";
        String password = "password";
        String newPassword = "newpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);
        traineeService.changeTraineePassword(username, newPassword, password);
        assertEquals(newPassword, trainee.getUser().getPassword());
        verify(traineeRepository, times(1)).updateTrainee(trainee);
    }

    @Test
    public void testChangeTraineePasswordFailure() {
        String username = "johndoe";
        String newPassword = "newpassword";
        String password = "wrongpassword";
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            traineeService.changeTraineePassword(username, newPassword, password);
        });
    }

    @Test
    public void testSetTraineeActiveStatusSuccess() {
        String username = "Pedro.Garcia";
        String password = "password";
        boolean isActive = true;
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword(password);
        traineeService.setTraineeActiveStatus(username, password, isActive);
        assertEquals(isActive, trainee.getUser().isActive());
        verify(traineeRepository, times(1)).updateTrainee(trainee);
    }

    @Test
    public void testSetTraineeActiveStatusFailure() {
        String username = "johndoe";
        String password = "wrongpassword";
        boolean isActive = true;
        when(traineeRepository.getTraineeByUsername(username)).thenReturn(trainee);
        trainee.getUser().setPassword("password");
        assertThrows(SecurityException.class, () -> {
            traineeService.setTraineeActiveStatus(username, password, isActive);
        });
    }

}
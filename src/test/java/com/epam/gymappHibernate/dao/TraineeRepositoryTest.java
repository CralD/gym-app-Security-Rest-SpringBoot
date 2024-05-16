package com.epam.gymappHibernate.dao;

import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class TraineeRepositoryTest {
    @MockBean
    private EntityManager entityManager;

    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private UserRepository userRepository;

    @Mock
    private TypedQuery<Trainee> query;

    @BeforeEach
    public void setUp() {

    }

    @Test
    @Transactional
    public void testSaveTrainee() {
        Trainee trainee = new Trainee();
        traineeRepository.saveTrainee(trainee);
        verify(entityManager).persist(trainee);
    }
    @Test
    public void testSaveTrainee2() {
        User user = new User();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setUserName("testuser");
        user.setPassword("password");
        user.setActive(true);


        userRepository.saveUser(user);


        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("123 Test St");
        trainee.setUser(user);


        traineeRepository.saveTrainee(trainee);


        Trainee savedTrainee = traineeRepository.getTraineeByUsername(user.getUserName());


        assertNotNull(savedTrainee);
        assertEquals(trainee.getTraineeID(), savedTrainee.getTraineeID());
        assertEquals(trainee.getAddress(), savedTrainee.getAddress());
        assertEquals(trainee.getUser().getUserName(), savedTrainee.getUser().getUserName());
    }

    @Test
    @Transactional
    public void testUpdateTrainee() {
        Trainee trainee = new Trainee();
        traineeRepository.updateTrainee(trainee);
        verify(entityManager).merge(trainee);
    }

    @Test
    @Transactional
    public void testDeleteTraineeByUsername() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        when(entityManager.createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainee);

        traineeRepository.deleteTraineeByUsername(username);

        verify(entityManager).remove(trainee);
    }

    @Test
    public void testDeleteTraineeByUsernameNotFound() {
        String username = "testuser";
        when(entityManager.createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new NoResultException());

        traineeRepository.deleteTraineeByUsername(username);

        verify(entityManager, never()).remove(any(Trainee.class));
    }

    @Test
    public void testGetTraineeByUsername() {
        String username = "testuser";
        Trainee trainee = new Trainee();
        when(entityManager.createQuery("SELECT t FROM Trainee t WHERE t.user.userName = :username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainee);

        Trainee result = traineeRepository.getTraineeByUsername(username);

        assertEquals(trainee, result);
    }

    @Test
    public void testGetTraineeByUsernameNotFound() {
        String username = "testuser";
        when(entityManager.createQuery("SELECT t FROM Trainee t WHERE t.user.userName = :username", Trainee.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new NoResultException());

        Trainee result = traineeRepository.getTraineeByUsername(username);

        assertNull(result);
    }

    @Test
    public void testFindAll() {


        when(entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class)).thenReturn(query);


        List<Trainee> result = traineeRepository.findAll();

        assertEquals(6, result.size());

    }

}
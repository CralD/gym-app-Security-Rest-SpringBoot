package com.epam.gymappHibernate.dao;



import com.epam.gymappHibernate.entity.Trainee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TraineeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public TraineeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Transactional
    public void saveTrainee(Trainee trainee){
        entityManager.persist(trainee);
    }
    @Transactional
    public void updateTrainee(Trainee trainee){
        entityManager.merge(trainee);
    }
    @Transactional
    public void deleteTraineeByUsername(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery(
                    "SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class
            );
            query.setParameter("username", username);
            Trainee trainee = query.getSingleResult();
            entityManager.remove(trainee);
        } catch (NoResultException e) {
            // Handle exception: trainee not found
        }
    }
    public Trainee getTraineeByUsername(String username) {
        try {
            TypedQuery<Trainee> query = entityManager.createQuery(
                    "SELECT t FROM Trainee t WHERE t.user.userName = :username", Trainee.class
            );
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // or throw a custom exception if preferred
        }
    }
    public List<Trainee> findAll() {
        TypedQuery<Trainee> query = entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class);
        return query.getResultList();
    }

}







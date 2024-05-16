package com.epam.gymappHibernate.dao;


import com.epam.gymappHibernate.entity.Trainer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TrainerRepository {
    private EntityManager entityManager;

    public TrainerRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void saveTrainer(Trainer trainer){
        entityManager.persist(trainer);
    }
    @Transactional
    public void updateTrainer(Trainer trainer){
        entityManager.merge(trainer);
    }



    public Trainer getTrainerByUsername(String username) {
        try {
            TypedQuery<Trainer> query = entityManager.createQuery(
                    "SELECT t FROM Trainer t WHERE t.user.username = :username", Trainer.class
            );
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // or throw a custom exception if preferred
        }
    }
    public List<Trainer> findAll() {
        TypedQuery<Trainer> query = entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class);
        return query.getResultList();
    }
}

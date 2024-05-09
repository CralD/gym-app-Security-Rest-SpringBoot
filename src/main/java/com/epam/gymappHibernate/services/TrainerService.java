package com.epam.gymappHibernate.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@Service
public class TrainerService {
   /* private Storage<Trainer> trainerDao;
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);

    @Autowired
    public TrainerService(Storage<Trainer> trainerDao) {
        this.trainerDao = trainerDao;
    }

    public Trainer getTrainerById(Long id) {
        return trainerDao.getById(id);
    }

    public void saveTrainer(Long id, Trainer trainer) {
        String username = generateUsername(trainer.getFirstName(), trainer.getLastName());
        String password = generatePassword();
        trainer.setUserName(username);
        trainer.setPassword(password);
        trainerDao.save(id, trainer);
        logger.info("Trainer saved with id: {}", id);
    }

    public void updateTrainer(Long id, Trainer trainer) {
        trainerDao.update(id, trainer);
        logger.info("Trainer updated with id: {}", id);
    }

    private String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;
        while (trainerDao.existByuserName(username)) {
            username = baseUsername + suffix++;
        }
        return username;
    }
    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        return random.ints(48, 122)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .mapToObj(i -> String.valueOf((char) i))
                .limit(10)
                .collect(Collectors.joining());
    }*/

}

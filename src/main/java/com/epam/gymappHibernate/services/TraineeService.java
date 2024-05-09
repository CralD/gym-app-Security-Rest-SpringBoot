package com.epam.gymappHibernate.services;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.stream.Collectors;


@Service
public class TraineeService {

    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);


  /*  public void getTraineeById(Long id) {
        return
    }

    public void saveTrainee() {

        logger.info("Trainee saved with id: {}", id);
    }

    public void updateTrainee(Long id, Trainee trainee) {
        String username = generateUsername(trainee.getFirstName(), trainee.getLastName());
        String password = generatePassword();
        trainee.setUserName(username);
        trainee.setPassword(password);
        traineeDao.update(id, trainee);
        logger.info("Trainee updated with id: {}", id);
    }

    public void deleteTrainee(Long id) {
        traineeDao.delete(id);
        logger.info("Trainee deleted with id: {}", id);
    }
    private String generateUsername(String firstName, String lastName) {
        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int suffix = 1;
        while (traineeDao.existByuserName(username)) {
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
    }
*/

}

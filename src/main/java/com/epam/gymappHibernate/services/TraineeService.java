package com.epam.gymappHibernate.services;


import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.User;
import com.epam.gymappHibernate.util.PasswordGenerator;
import com.epam.gymappHibernate.util.UsernameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TraineeService {
    private final TraineeRepository traineeRepository;

    //private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);


    @Autowired
    public TraineeService(TraineeRepository traineeRepository) {
        this.traineeRepository = traineeRepository;
    }

    public void createTrainee(Trainee trainee) {
        User user = trainee.getUser();
        List<String> existingUsernames = getAllUsernames();
        String username = UsernameGenerator.generateUsername(user.getFirstName(), user.getLastName(), existingUsernames);
        user.setUserName(username);
        String password = PasswordGenerator.generatePassword();
        user.setPassword(password);
        traineeRepository.saveTrainee(trainee);
    }
    private List<String> getAllUsernames() {
        return traineeRepository.findAll().stream()
                .map(trainee -> trainee.getUser().getUserName())
                .collect(Collectors.toList());
    }
}

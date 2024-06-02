package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.services.TraineeService;
import com.epam.gymappHibernate.services.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final TraineeService traineeService;

    private final TrainerService trainerService;
    @Autowired
    public UserController(TraineeService traineeService, TrainerService trainerService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(
            @RequestHeader(name = "username") String username,
            @RequestHeader(name = "password") String password) {


        if (traineeService.authenticate(username, password) || trainerService.authenticate(username, password)) {
            return new ResponseEntity<>("Welcome", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Wrong username or password", HttpStatus.UNAUTHORIZED);
        }
    }
    @PutMapping("/{username}/password")
    public ResponseEntity<String> updatePassword(
            @PathVariable("username") String username,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("password") String password) {

        if (traineeService.authenticate(username, password)) {
            traineeService.changeTraineePassword(username, newPassword, password);
            return new ResponseEntity<>("Password updated successfully", HttpStatus.OK);
        }  else {
            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        }
    }
}

package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dto.TraineeDto;
import com.epam.gymappHibernate.dto.TrainerDto;
import com.epam.gymappHibernate.dto.UserDto;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.TrainingType;
import com.epam.gymappHibernate.entity.User;
import com.epam.gymappHibernate.services.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainees")
public class TraineeController {

    private final TraineeService traineeService;

    private final TrainerRepository trainerRepository;
    @Autowired
    public TraineeController(TraineeService traineeService, TrainerRepository trainerRepository) {
        this.traineeService = traineeService;
        this.trainerRepository = trainerRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> RegisterTrainee(@RequestBody TraineeDto request){
        Trainee trainee = traineeService.mapToTrainee(request);

        traineeService.createTrainee(trainee);

       UserDto responseDTO = new UserDto(request.getFirstName(), request.getLastName());
        responseDTO.setUsername(trainee.getUser().getUserName());
        responseDTO.setPassword(trainee.getUser().getPassword());

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TraineeDto> getTraineeByUsername(@PathVariable("username") String username,@RequestParam("password") String password) {
        Trainee trainee = traineeService.getTraineeByUsername(username,password);
        TraineeDto traineeDto = traineeService.convertToTraineeDto(trainee);
            return ResponseEntity.ok(traineeDto);

    }

    @PutMapping("/{username}")
    public ResponseEntity<TraineeDto> updateTraineeByUsername(@PathVariable("username")String username,@RequestParam("password") String password,@RequestBody TraineeDto traineeDto){
        Trainee trainee = traineeService.updateTraineeProfile(username, password, traineeDto);
        TraineeDto updateTraineeDto = traineeService.convertToTraineeDto(trainee);

        return ResponseEntity.ok(updateTraineeDto);

    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTraineeByUsername(@PathVariable("username")String username,@RequestParam("password") String password){

        if(traineeService.authenticate(username, password)){
            traineeService.deleteTrainee(username,password);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

    }



}

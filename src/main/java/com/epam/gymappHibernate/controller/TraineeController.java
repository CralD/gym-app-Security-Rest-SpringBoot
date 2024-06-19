package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dao.TrainerRepository;
import com.epam.gymappHibernate.dto.*;
import com.epam.gymappHibernate.entity.*;
import com.epam.gymappHibernate.exception.AuthenticationException;
import com.epam.gymappHibernate.exception.NoTrainingsFoundException;
import com.epam.gymappHibernate.services.TraineeService;
import com.epam.gymappHibernate.services.TrainerService;
import com.epam.gymappHibernate.services.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainees")
@Api(tags = "Trainees")
public class TraineeController {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    private final TrainerRepository trainerRepository;
    @Autowired
    public TraineeController(TraineeService traineeService, TrainerRepository trainerRepository, TrainerService trainerService,TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerRepository = trainerRepository;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostMapping
    @ApiOperation(value = "Register a trainee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee registered successfully"),
            @ApiResponse(code = 400, message = "Invalid trainee data")
    })
    public ResponseEntity<UserDto> RegisterTrainee(@RequestBody TraineeDto request){
        Trainee trainee = traineeService.mapToTrainee(request);

        traineeService.createTrainee(trainee);

       UserDto responseDTO = new UserDto(request.getFirstName(), request.getLastName());
        responseDTO.setUsername(trainee.getUser().getUserName());
        responseDTO.setPassword(trainee.getUser().getPassword());

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get a trainee by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee found"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<TraineeDto> getTraineeByUsername(@PathVariable("username") String username,@RequestParam("password") String password) {
        try {
            Trainee trainee = traineeService.getTraineeByUsername(username, password);
            TraineeDto traineeDto = traineeService.convertToTraineeDto(trainee);
            return ResponseEntity.ok(traineeDto);

        } catch (SecurityException e) {
           throw new AuthenticationException("Trainee not found,invalid username or password");
        }
    }


    @PutMapping("/{username}")
    @ApiOperation(value = "Update a trainee by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee updated successfully"),
            @ApiResponse(code = 400, message = "Invalid trainee data"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<TraineeDto> updateTraineeByUsername(@PathVariable("username")String username,@RequestParam("password") String password,@RequestBody TraineeDto traineeDto){
        if(traineeService.authenticate(username, password)){
        Trainee trainee = traineeService.updateTraineeProfile(username, password, traineeDto);
        TraineeDto updateTraineeDto = traineeService.convertToTraineeDto(trainee);

        return ResponseEntity.ok(updateTraineeDto);
        }else {
            throw new AuthenticationException("Invalid username or password");
        }

    }

    @DeleteMapping("/{username}")
    @ApiOperation(value = "Delete a trainee by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee deleted successfully"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<?> deleteTraineeByUsername(@PathVariable("username")String username,@RequestParam("password") String password){

        if(traineeService.authenticate(username, password)){
            traineeService.deleteTrainee(username,password);
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            throw new AuthenticationException("Invalid username or password");
        }

    }
    @GetMapping("/unassigned/{username}")
    @ApiOperation(value = "Get unassigned trainers for a trainee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Unassigned trainers found"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<List<TrainerDtoResponse>> getUnassignedTrainers(@PathVariable String username, @RequestParam("password") String password) {
        if (!traineeService.authenticate(username, password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        List<TrainerDtoResponse> trainers = trainerService.findUnassignedTrainers(username);
        return ResponseEntity.ok(trainers);
    }
    @PutMapping("/update-trainers/{username}")
    @ApiOperation(value = "Update trainers for a trainee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainers updated successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<List<TrainerDtoResponse>> updateTraineeTrainers(@PathVariable("username")String username, @RequestBody UpdateTrainer request) {

        List<TrainerDtoResponse> updatedTrainers = trainerService.updateTraineeTrainers(username, request.getTrainerUsernames());
        return ResponseEntity.ok(updatedTrainers);
    }

    @GetMapping("/trainings")
    @ApiOperation(value = "Get trainings for a trainee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainings found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<List<TrainingDtoResponse>> getTraineeTrainings(
            @RequestParam String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) String trainingType) {

        List<Training> trainings = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
        if (trainings.isEmpty()) {
            throw new NoTrainingsFoundException("No trainings found for the specified trainee");
        }
        List<TrainingDtoResponse> response = trainings.stream()
                .map(trainingService::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
    @PatchMapping("/{username}")
    @ApiOperation(value = "Activate/deactivate a trainee")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee activated/deactivated successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    public ResponseEntity<Void> activateTrainee(@PathVariable("username")String username, @RequestParam String password,@RequestBody AuntheticatioDto request) {
        if (!traineeService.authenticate(username, password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        traineeService.setTraineeActiveStatus(username, password, request.isActive());
        return ResponseEntity.ok().build();
    }



}

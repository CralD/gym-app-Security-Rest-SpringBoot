package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dto.*;
import com.epam.gymappHibernate.entity.*;
import com.epam.gymappHibernate.exception.AuthenticationException;
import com.epam.gymappHibernate.exception.NoTrainingsFoundException;
import com.epam.gymappHibernate.services.TrainerService;
import com.epam.gymappHibernate.services.TrainingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trainers")
@Api(tags = "Trainers")
public class TrainerController {

    private final TrainerService trainerService;
    private final TrainingService trainingService;
    @Autowired
    public TrainerController(TrainerService trainerService,TrainingService trainingService) {
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    @PostMapping("/register")
    @ApiOperation(value = "Register a trainer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer registered successfully"),
            @ApiResponse(code = 400, message = "Invalid trainer data")
    })
    public ResponseEntity<UserDto> saveTrainerByUsername(@RequestBody TrainerDto trainerDto) {

        Trainer trainer = trainerService.mapToTrainer(trainerDto);
        trainerService.createTrainer(trainer);

        UserDto responseDto = new UserDto(trainerDto.getFirstName(),trainerDto.getLastName());
        responseDto.setUsername(trainer.getUser().getUserName());
        responseDto.setPassword(trainer.getUser().getPassword());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    @ApiOperation(value = "Get a trainer by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer found"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    public ResponseEntity<TrainerDto> getTraineeByUsername(@PathVariable("username") String username,@RequestParam("password") String password) {
        try {
        Trainer trainer = trainerService.getTrainerByUsername(username,password);
        TrainerDto trainerDto = trainerService.trainerDtoConverter(trainer);
        return ResponseEntity.ok(trainerDto);
        } catch (SecurityException e) {
            throw new AuthenticationException("Trainer not found,invalid username or password");
        }

    }
    @PutMapping("/{username}")
    @ApiOperation(value = "Update a trainer by username")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer updated successfully"),
            @ApiResponse(code = 400, message = "Invalid trainer data"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    public ResponseEntity<TrainerDto> updateTraineeByUsername(@PathVariable("username")String username,@RequestParam("password") String password,@RequestBody TrainerDto trainerDto){
        if(trainerService.authenticate(username, password)){
        Trainer trainer = trainerService.updateTrainerProfile(username, password, trainerDto);
        TrainerDto updateTrainerDto = trainerService.trainerDtoConverter(trainer);

        return ResponseEntity.ok(updateTrainerDto);
        }else {
            throw new AuthenticationException("Invalid username or password");
        }

    }
    @GetMapping("/trainings")
    @ApiOperation(value = "Get trainings for a trainer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainings found"),
            @ApiResponse(code = 400, message = "Invalid request")
    })
    public ResponseEntity<List<TrainingDtoResponse>> getTrainerTrainings(
            @RequestParam String username,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date toDate,
            @RequestParam(required = false) String traineeName) {

        List<Training> trainings = trainingService.getTrainerTrainings(username, fromDate, toDate, traineeName);
        if (trainings.isEmpty()) {
            throw new NoTrainingsFoundException("No trainings found for the specified trainer");
        }
        List<TrainingDtoResponse> response = trainings.stream()
                .map(trainingService::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/activate/{username}")
    @ApiOperation(value = "Activate/deactivate a trainer")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer activated/deactivated successfully"),
            @ApiResponse(code = 400, message = "Invalid request"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    public ResponseEntity<Void> activateTrainer(@PathVariable("username")String username, @RequestParam String password,@RequestBody AuntheticatioDto request) {
        if (!trainerService.authenticate(username, password)) {
            throw new AuthenticationException("Invalid username or password");
        }
        trainerService.setTrainerActiveStatus( username, password, request.isActive());
        return ResponseEntity.ok().build();
    }

}

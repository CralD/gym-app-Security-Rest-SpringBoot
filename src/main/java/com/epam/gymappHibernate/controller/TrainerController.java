package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dto.TraineeDto;
import com.epam.gymappHibernate.dto.TrainerDto;
import com.epam.gymappHibernate.dto.UserDto;
import com.epam.gymappHibernate.entity.Trainee;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.TrainingType;
import com.epam.gymappHibernate.entity.User;
import com.epam.gymappHibernate.services.TraineeService;
import com.epam.gymappHibernate.services.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trainers")
public class TrainerController {

    private final TrainerService trainerService;
    @Autowired
    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> getTrainerByUsername(@RequestBody TrainerDto trainerDto) {

        Trainer trainer = trainerService.mapToTrainer(trainerDto);
        trainerService.createTrainer(trainer);

        UserDto responseDto = new UserDto(trainerDto.getFirstName(),trainerDto.getLastName());
        responseDto.setUsername(trainer.getUser().getUserName());
        responseDto.setPassword(trainer.getUser().getPassword());

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/{username}")
    public ResponseEntity<TrainerDto> getTraineeByUsername(@PathVariable("username") String username,@RequestParam("password") String password) {
        Trainer trainer = trainerService.getTrainerByUsername(username,password);
        TrainerDto trainerDto = trainerService.trainerDtoConverter(trainer);
        return ResponseEntity.ok(trainerDto);

    }
    @PutMapping("/{username}")
    public ResponseEntity<TrainerDto> updateTraineeByUsername(@PathVariable("username")String username,@RequestParam("password") String password,@RequestBody TrainerDto trainerDto){
        Trainer trainer = trainerService.updateTrainerProfile(username, password, trainerDto);
        TrainerDto updateTrainerDto = trainerService.trainerDtoConverter(trainer);

        return ResponseEntity.ok(updateTrainerDto);

    }
}

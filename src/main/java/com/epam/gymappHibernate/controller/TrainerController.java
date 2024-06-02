package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dto.TraineeDto;
import com.epam.gymappHibernate.dto.TrainerDto;
import com.epam.gymappHibernate.dto.UserDto;
import com.epam.gymappHibernate.entity.Trainer;
import com.epam.gymappHibernate.entity.TrainingType;
import com.epam.gymappHibernate.entity.User;
import com.epam.gymappHibernate.services.TraineeService;
import com.epam.gymappHibernate.services.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

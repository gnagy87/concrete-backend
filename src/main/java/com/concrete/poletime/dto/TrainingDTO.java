package com.concrete.poletime.dto;

import com.concrete.poletime.training.Training;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDTO {
  private Long id;
  private Date trainingFrom;
  private Date trainingTo;
  private TrainingHall hall;
  private TrainingLevel level;
  private TrainingType type;
  private Long organizerId;
  private int personLimit;
  private int numberOfApplicants;
  private boolean isHeld;

  public TrainingDTO(Training training) {
    this.id = training.getId();
    this.trainingFrom = training.getTrainingFrom();
    this.trainingTo = training.getTrainingTo();
    this.hall = training.getHall();
    this.level = training.getLevel();
    this.type = training.getType();
    this.organizerId = training.getOrganizerId();
    this.personLimit = training.getPersonLimit();
    this.numberOfApplicants = training.getPoleUsers().size();
    this.isHeld = training.isHeld();
  }
}

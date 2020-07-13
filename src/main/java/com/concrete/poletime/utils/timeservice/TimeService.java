package com.concrete.poletime.utils.timeservice;

import com.concrete.poletime.exceptions.TrainingException;

import java.util.Date;

public interface TimeService {
  long trainingTimeCalculator(Date trainingFrom, Date trainingTo) throws TrainingException;
}

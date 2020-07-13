package com.concrete.poletime.utils.timeservice;

import com.concrete.poletime.exceptions.TrainingException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TimeServiceImpl implements TimeService {

  @Override
  public long trainingTimeCalculator(Date trainingFrom, Date trainingTo) throws TrainingException {
    if (trainingTo.before(trainingFrom)) throw new TrainingException("trainingTo can not be smaller than trainingFrom");
    return (trainingTo.getTime() - trainingFrom.getTime()) / (60 * 1000);
  }

}

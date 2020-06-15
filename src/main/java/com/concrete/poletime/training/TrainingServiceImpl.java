package com.concrete.poletime.training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrainingServiceImpl implements TrainingService {

    private TrainingRepository trainingRepo;

    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepo) {
        this.trainingRepo = trainingRepo;
    }
}

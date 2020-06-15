package com.concrete.poletime.training;

import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, name = "training_from")
    private Date trainingFrom;
    @Column(nullable = false, name = "training_to")
    private Date trainingTo;
    @Column(nullable = false)
    private TrainingHall hall;
    private int limit;
    @Column(nullable = false)
    private TrainingType type;
    @Column(nullable = false)
    private TrainingLevel level;
    @Column(nullable = false, name = "organizer_id")
    private Long organizerId;
    @Column(name = "is_held")
    private boolean isHeld;
}

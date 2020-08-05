package com.concrete.poletime.training;

import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
    @Enumerated(EnumType.STRING)
    private TrainingHall hall;
    @Column(name = "person_limit")
    private int personLimit;
    @Enumerated(EnumType.STRING)
    private TrainingType type;
    @Enumerated(EnumType.STRING)
    private TrainingLevel level;
    @Column(nullable = false, name = "organizer_id")
    private Long organizerId;
    @Column(name = "is_held")
    private boolean isHeld;
    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private Date createdAt;
    @JsonBackReference
    @ManyToMany(mappedBy = "trainings")
    private Set<PoleUser> poleUsers;

    public Training(Date trainingFrom, Date trainingTo, TrainingHall hall, int personLimit, TrainingType type, TrainingLevel level, Long organizerId) {
        this.trainingFrom = trainingFrom;
        this.trainingTo = trainingTo;
        this.hall = hall;
        this.personLimit = personLimit;
        this.type = type;
        this.level = level;
        this.organizerId = organizerId;
        this.isHeld = false;
        this.poleUsers = new HashSet<>();
    }
}

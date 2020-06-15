package com.concrete.poletime.training;

import com.concrete.poletime.user.PoleUser;
import com.concrete.poletime.utils.TrainingHall;
import com.concrete.poletime.utils.TrainingLevel;
import com.concrete.poletime.utils.TrainingType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
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
    @Basic(optional = false)
    @Column(name = "created_at", insertable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @ManyToMany(mappedBy = "trainings")
    private Set<PoleUser> poleUsers;
}

package com.concrete.poletime.training;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingRepository extends CrudRepository<Training, Long> {
    @Query(
            value = "SELECT * FROM trainings " +
                    "WHERE hall = ?1 AND " +
                    "((training_to > ?2 AND training_to <= ?3) OR " +
                    "(training_from >= ?2 AND training_from < ?3) OR " +
                    "(training_from <= ?2 AND training_to >= ?3)) " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Optional<Training> findTrainingInSameTime(String hall, Date trainingFrom, Date trainingTo);

    @Query(
        value = "SELECT * FROM trainings " +
                "WHERE type = 'GROUP' " +
                "AND training_from >= ?1 " +
                "AND training_to <= ?2 " +
                "ORDER BY training_from, training_to",
        nativeQuery = true
    )
    Optional<List<Training>> loadGroupTrainings(Date fromDate, Date toDate);

    @Query(
        value = "SELECT * FROM trainings " +
                "WHERE type != 'GROUP' " +
                "ORDER BY training_from, training_to",
        nativeQuery = true
    )
    Optional<List<Training>> loadNonGroupTrainings();

    @Query(
        value = "SELECT * FROM trainings " +
            "AND training_from >= ?1 " +
            "AND training_to <= ?2 " +
            "ORDER BY training_from, training_to",
        nativeQuery = true
    )
    Optional<List<Training>> loadAllTrainingsBetween(Date fromDate, Date toDate);
}

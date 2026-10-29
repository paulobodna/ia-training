package com.bodna.logic_hub.repositories;

import com.bodna.logic_hub.domain.sudoku.SudokuAttempt;
import com.bodna.logic_hub.repositories.projections.SudokuAttemptHistoryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface SudokuAttemptRepository extends JpaRepository<SudokuAttempt, Long> {
    @Query("SELECT s " +
        "FROM SudokuAttempt s " +
        "WHERE s.user.id = :userId AND s.completedAt >= :limitDate " +
        "ORDER BY completedAt DESC"
    )
    List<SudokuAttemptHistoryProjection> getUserHistory(
        @Param("userId") Long userId,
        @Param("limitDate") Instant limitDate
    );
}

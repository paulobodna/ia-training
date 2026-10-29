package com.bodna.logic_hub.domain.sudoku;

import com.bodna.logic_hub.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.internal.annotation.SuppressFBWarnings;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Duration;
import java.time.Instant;

@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = "User is a JPA entity association and must be stored by reference."
)
@Table(name = "attempts")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class SudokuAttempt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    private Long seed;
    private byte generatorVersion;
    private Difficulty difficulty;
    private byte tipsUsed;
    private Instant completedAt;

    @JdbcTypeCode(SqlTypes.INTERVAL_SECOND)
    private Duration timeElapsed;
    private short xpObtained;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SudokuAttempt(
        Long seed,
        byte generatorVersion,
        Difficulty difficulty,
        byte tipsUsed,
        Instant completedAt,
        Duration timeElapsed,
        short xpObtained,
        User user
    ) {
        this.seed = seed;
        this.generatorVersion = generatorVersion;
        this.difficulty = difficulty;
        this.tipsUsed = tipsUsed;
        this.completedAt = completedAt;
        this.timeElapsed = timeElapsed;
        this.xpObtained = xpObtained;
        this.user = user;
    }
}

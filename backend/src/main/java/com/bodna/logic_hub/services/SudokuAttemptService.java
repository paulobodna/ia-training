package com.bodna.logic_hub.services;

import com.bodna.logic_hub.domain.sudoku.SudokuAttempt;
import com.bodna.logic_hub.domain.user.User;
import com.bodna.logic_hub.dtos.SudokuAttemptHistoryResponse;
import com.bodna.logic_hub.dtos.SudokuAttemptRequest;
import com.bodna.logic_hub.repositories.SudokuAttemptRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class SudokuAttemptService {

    @Autowired
    private SudokuAttemptRepository sudokuRepository;
    @Autowired
    private UserService userService;

    @Transactional
    public void finishAttempt(SudokuAttemptRequest attemptRequest) {
        User user = userService.getCurrentUser();

        SudokuAttempt sudokuGame = createSudokuAttempt(attemptRequest, user);

        sudokuRepository.save(sudokuGame);

        user.setExperience(user.getExperience() + 20);
    }

    public List<SudokuAttemptHistoryResponse> getPlayerAttempts() {
        User user = userService.getCurrentUser();
        Instant sixMonthsAgo = ZonedDateTime.now(ZoneOffset.UTC).minusMonths(6).toInstant();

        return sudokuRepository.getUserHistory(user.getId(), sixMonthsAgo)
            .stream()
            .map(attempt -> new SudokuAttemptHistoryResponse(
                attempt.getSeed(),
                attempt.getGeneratorVersion(),
                attempt.getDifficulty(),
                attempt.getTipsUsed(),
                attempt.getCompletedAt(),
                attempt.getTimeElapsed(),
                attempt.getXpObtained()
            ))
            .toList();
    }

    private SudokuAttempt createSudokuAttempt(SudokuAttemptRequest attemptRequest, User user) {
        return new SudokuAttempt(
            attemptRequest.seed(),
            attemptRequest.generatorVersion(),
            attemptRequest.difficulty(),
            attemptRequest.tipsUsed(),
            attemptRequest.completedAt(),
            attemptRequest.timeElapsed(),
            (short) 20, //TODO: desenvolver experiência
            user
        );
    }
}

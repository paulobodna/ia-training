package com.bodna.logic_hub.controllers;

import com.bodna.logic_hub.dtos.SudokuAttemptHistoryResponse;
import com.bodna.logic_hub.dtos.SudokuAttemptRequest;
import com.bodna.logic_hub.services.SudokuAttemptService;
import com.bodna.logic_hub.services.SudokuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class SudokuController {

    @Autowired
    private SudokuService sudokuService;
    @Autowired
    private SudokuAttemptService sudokuAttemptService;

    @PostMapping("/sudoku")
    public ResponseEntity<?> saveSudokuGame(@Valid @RequestBody SudokuAttemptRequest request) {
        this.sudokuService.submitSudokuAttempt(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/history")
    public ResponseEntity<List<SudokuAttemptHistoryResponse>> getUserHistory() {
        List<SudokuAttemptHistoryResponse> sudokuHistory = this.sudokuAttemptService.getPlayerAttempts();
        return ResponseEntity.ok(sudokuHistory);
    }
}
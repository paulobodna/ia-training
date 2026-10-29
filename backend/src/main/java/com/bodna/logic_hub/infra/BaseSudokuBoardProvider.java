package com.bodna.logic_hub.infra;

import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public final class BaseSudokuBoardProvider {

    private final List<SudokuBoard> boards;

    public BaseSudokuBoardProvider(
        @Value("classpath:utils/baseSudoku.json") Resource jsonResource,
        ObjectMapper objectMapper
    ) {
        try {
            byte[][][] baseBoards = objectMapper.readValue(
                jsonResource.getInputStream(),
                byte[][][].class
            );

            this.boards = List.copyOf(Arrays.stream(baseBoards)
                .map(SudokuBoard::new)
                .toList()
            );


        } catch (IOException e) {
            throw new RuntimeException("Failed to get base Sudoku boards from file", e);
        }
    }

    public List<SudokuBoard> getBaseSudokuBoards() {
        return boards;
    }
}

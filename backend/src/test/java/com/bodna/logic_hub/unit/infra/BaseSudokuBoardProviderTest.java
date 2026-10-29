package com.bodna.logic_hub.unit.infra;

import com.bodna.logic_hub.domain.sudoku.SudokuBoard;
import com.bodna.logic_hub.infra.BaseSudokuBoardProvider;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseSudokuBoardProviderTest {

    private BaseSudokuBoardProvider provider;

    @Test
    void shouldReturnValidAndCompleteSudokuBoards() {
        provider = new BaseSudokuBoardProvider(
            new ClassPathResource("utils/baseSudoku.json"),
            new ObjectMapper()
        );

        List<SudokuBoard> boards = provider.getBaseSudokuBoards();

        for (SudokuBoard board : boards) {
            assertTrue(board.isComplete() && board.isValid());
        }
    }

    @Test
    void shouldThrowRuntimeExceptionWhenPathNotFound() {
        assertThrows(
            RuntimeException.class,
            () -> provider = new BaseSudokuBoardProvider(
                new ClassPathResource("test/non-existing-file.json"),
                new ObjectMapper()
            )
        );
    }
}
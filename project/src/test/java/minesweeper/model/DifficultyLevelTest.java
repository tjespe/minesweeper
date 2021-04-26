package minesweeper.model;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DifficultyLevelTest {

    @Test
    public void testGetLabel() {
        Assertions.assertEquals("Normal", DifficultyLevel.NORMAL.getLabel());
    }

    @Test
    public void testGetBombs() {
        Assertions.assertEquals(10, DifficultyLevel.EASY.getBombs());
    }

    @Test
    public void testGetHeight() {
        Assertions.assertEquals(8, DifficultyLevel.EASY.getHeight());
    }

    @Test
    public void testGetWidth() {
        Assertions.assertEquals(21, DifficultyLevel.HARD.getWidth());
    }

    @Test
    public void testGetAll() {
        Assertions.assertEquals(Arrays.asList(DifficultyLevel.EASY, DifficultyLevel.NORMAL, DifficultyLevel.HARD),
                DifficultyLevel.getAll());
    }

    @Test
    public void testGetAllLabels() {
        Assertions.assertTrue(Arrays.asList("Easy", "Normal", "Hard").equals(DifficultyLevel.getAllLabels()));
    }

    @Test
    public void testGetByLabel() {
        Assertions.assertEquals(DifficultyLevel.EASY, DifficultyLevel.getByLabel("Easy"));
        Assertions.assertEquals(DifficultyLevel.NORMAL, DifficultyLevel.getByLabel("Normal"));
        Assertions.assertEquals(DifficultyLevel.HARD, DifficultyLevel.getByLabel("Hard"));
        Assertions.assertEquals(null, DifficultyLevel.getByLabel("Non-existent"));
    }

}

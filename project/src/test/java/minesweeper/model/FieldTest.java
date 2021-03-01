package minesweeper.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldTest {

	private Field nonBombField;
	private Field bombField;

	@BeforeEach
	public void setup() {
		nonBombField = new Field(false);
		bombField = new Field(true);
	}

	@Test
	public void testConstructor() {
		Assertions.assertEquals(true, bombField.getHasBomb());
		Assertions.assertEquals(false, nonBombField.getHasBomb());
		Assertions.assertEquals(false, bombField.getIsFlagged());
		Assertions.assertEquals(false, bombField.getIsOpened());
		Assertions.assertEquals(false, nonBombField.getIsFlagged());
		Assertions.assertEquals(false, nonBombField.getIsOpened());
	}

	@Test
	public void testToggleFlag() {
		bombField.toggleFlag();
		Assertions.assertEquals(true, bombField.getIsFlagged());
		bombField.toggleFlag();
		Assertions.assertEquals(false, bombField.getIsFlagged());
	}

	@Test
	public void testOpen() {
		bombField.toggleFlag();
		Assertions.assertThrows(IllegalStateException.class, () -> {
			bombField.open();
		});
		bombField.toggleFlag();
		bombField.open();
		Assertions.assertThrows(IllegalStateException.class, () -> {
			bombField.open();
		});
		Assertions.assertEquals(true, bombField.getIsOpened());
	}

}

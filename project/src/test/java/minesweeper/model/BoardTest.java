package minesweeper.model;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardTest extends TestWithJavaFXTimeline {

	/**
	 * Helper method for checking that a list of coordinates contain the expected
	 * coordinates. Ignores order, as it should. Checks that `actual` contains the
	 * expected coordinates, and no other coordinates.
	 */
	private void assertCoordsListsAreEqual(int[][] expected, Collection<int[]> actual) {
		if (expected.length != actual.size())
			throw new AssertionError("Lengths differ");
		for (int[] expectedCoords : expected) {
			if (actual.stream().noneMatch(actualCoords -> Arrays.equals(expectedCoords, actualCoords)))
				throw new AssertionError(
						"Expected to find " + Arrays.toString(expectedCoords) + " but could not find it");
		}
	}

	private int[] getFieldFromBoard(Board board, boolean shouldHaveBomb) {
		for (int col = 0; col < board.getWidth(); col++) {
			for (int row = 0; row < board.getHeight(); row++) {
				if (board.getFieldHasBomb(col, row) == shouldHaveBomb) {
					return new int[] { col, row };
				}
			}
		}
		throw new Error("Could not find a field that fit the requirements");
	}

	@Test
	public void testConstructor() {
		int height = 10, width = 10, bombs = 10;
		Board board = new Board(height, width, bombs);
		int countedBombs = 0;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if (board.getFieldHasBomb(x, y))
					countedBombs++;
		Assertions.assertEquals(bombs, countedBombs);
	}

	@Test
	public void testConstructionFromSerializedState() {
		String serializedState = "02:34\n"
				//
				+ "uUuo\n"
				//
				+ "Uuuo\n"
				//
				+ "Fooo\n";
		Board board = new Board(serializedState);
		Assertions.assertEquals(3, board.getHeight());
		Assertions.assertEquals(4, board.getWidth());
		Assertions.assertFalse(board.getFieldHasBomb(0, 0));
		Assertions.assertTrue(board.getFieldHasBomb(0, 1));
		Assertions.assertThrows(IllegalStateException.class, () -> {
			board.openField(3, 0);
		});
		Assertions.assertEquals(Field.FLAGGED_WITH_BOMB, board.getFieldStatus(0, 2));
		Assertions.assertEquals(2, board.getRemainingFlags());
		Assertions.assertEquals(serializedState, board.getSerializedState());
	}

	@Test
	public void testGetAdjacentFields() {
		Board board = new Board(10, 10, 10);

		// Check top left corner
		int x1 = 0, y1 = 0;
		int[][] expectedAdjacent1 = { { 0, 1 }, { 1, 1 }, { 1, 0 } };
		assertCoordsListsAreEqual(expectedAdjacent1, board.getAdjacentFields(x1, y1));

		// Check a field along the left side
		int x2 = 0, y2 = 5;
		int[][] expectedAdjacent2 = { { 0, 4 }, { 1, 4 }, { 1, 5 }, { 1, 6 }, { 0, 6 } };
		assertCoordsListsAreEqual(expectedAdjacent2, board.getAdjacentFields(x2, y2));

		// Check the bottom right corner
		int x3 = 9, y3 = 9;
		int[][] expectedAdjacent3 = { { 8, 9 }, { 8, 8 }, { 9, 8 } };
		assertCoordsListsAreEqual(expectedAdjacent3, board.getAdjacentFields(x3, y3));

		// Check a field in the middle
		int x4 = 5, y4 = 5;
		int[][] expectedAdjacent4 = { { 4, 4 }, { 5, 4 }, { 6, 4 }, { 4, 5 }, { 4, 6 }, { 5, 6 }, { 6, 6 }, { 6, 5 } };
		assertCoordsListsAreEqual(expectedAdjacent4, board.getAdjacentFields(x4, y4));
	}

	@Test
	public void testCountAdjacentBombs() {
		// Create a 2x2 board with 3 bombs
		Board smallBoard = new Board(2, 2, 3);

		// Find the field that does not have a bomb
		int[] coords = getFieldFromBoard(smallBoard, false);

		// Check that the field has 3 adjacent bombs
		Assertions.assertEquals(3, smallBoard.countAdjacentBombs(coords[0], coords[1]));
	}

	@Test
	public void testOpenField() {
		// Create a 10x10 board with 1 bomb
		Board board = new Board(10, 10, 1);

		// Find the bomb
		int[] bombCoords = getFieldFromBoard(board, true);

		// Get an adjacent field of the bomb field
		int[] adjacCoords = board.getAdjacentFields(bombCoords[0], bombCoords[1]).iterator().next();

		// Get an adjacent field of the adjacent field that is not adjacent to the bomb
		int[] fieldToOpen = board.getAdjacentFields(adjacCoords[0], adjacCoords[1]).stream()
				.filter(coords -> !board.getFieldHasBomb(coords[0], coords[1])
						&& board.countAdjacentBombs(coords[0], coords[1]) == 0)
				.findFirst().orElseThrow();

		// Open the field without bomb
		board.openField(fieldToOpen[0], fieldToOpen[1]);

		// Check that adjacent fields were opened automatically
		for (int[] coords : board.getAdjacentFields(fieldToOpen[0], fieldToOpen[1]))
			Assertions.assertEquals(Field.OPENED, board.getFieldStatus(coords[0], coords[1]),
					"Expected " + Arrays.toString(coords) + " to be opened since it is adjacent to "
							+ Arrays.toString(fieldToOpen)
							+ " and does not have a bomb. Here is what the board looks like:\n" + board.toString());

		// Validate that the bomb was not opened automatically
		Assertions.assertEquals(Field.UNOPENED_WITH_BOMB, board.getFieldStatus(bombCoords[0], bombCoords[1]));

	}

	@Test
	public void testUserCanWin() {
		// Create a 2x2 board with 1 bomb
		Board board = new Board(2, 2, 1);

		// Check that user has not won yet
		Assertions.assertEquals(Board.PLAYING, board.getStatus());

		// Open all fields that do not have a bomb
		for (int x = 0; x < 2; x++)
			for (int y = 0; y < 2; y++)
				if (!board.getFieldHasBomb(x, y) && board.getFieldStatus(x, y) != Field.OPENED)
					board.openField(x, y);

		// Check that user won
		Assertions.assertEquals(Board.WON, board.getStatus());
	}

	@Test
	public void testUserCanLose() {
		// Create a 2x2 board with 1 bomb
		Board smallBoard = new Board(2, 2, 1);

		// Check that user has not lost yet
		Assertions.assertEquals(Board.PLAYING, smallBoard.getStatus());

		// Open the bomb field
		int[] coords = getFieldFromBoard(smallBoard, true);
		smallBoard.openField(coords[0], coords[1]);

		// Check that user lost
		Assertions.assertEquals(Board.LOST, smallBoard.getStatus());
	}

	@Test
	public void testBombsAutoOpen() {
		// Create a 2x2 board with 3 bombs
		Board board = new Board(2, 2, 3);

		// Check that user has not lost yet
		Assertions.assertEquals(Board.PLAYING, board.getStatus());

		// Find the field without a bomb
		int[] nonBombCoords = getFieldFromBoard(board, false);

		// Open a field with a bomb
		int[] bombCoords = getFieldFromBoard(board, true);
		board.openField(bombCoords[0], bombCoords[1]);

		// Check that all the bomb fields were automatically opened
		for (int[] coords : board.getAdjacentFields(nonBombCoords[0], nonBombCoords[1])) {
			Assertions.assertEquals(Field.BOMB, board.getFieldStatus(coords[0], coords[1]));
		}
	}

	@Test
	public void testGetBombCount() {
		int expectedBombs = 17;
		Board board = new Board(20, 20, expectedBombs);
		Assertions.assertEquals(expectedBombs, board.getBombCount());
	}

	@Test
	public void testGetRemainingFlags() {
		int bombs = 13;
		Board board = new Board(10, 10, bombs);
		Assertions.assertEquals(bombs, board.getRemainingFlags());
		board.toggleFlag(5, 5);
		Assertions.assertEquals(bombs - 1, board.getRemainingFlags());
	}

	@Test
	public void testGetSerializedState() {
		int height = 10;
		int width = 10;
		int bombs = 13;
		Board board = new Board(height, width, bombs);
		int[] bombCoords = getFieldFromBoard(board, true);
		board.toggleFlag(bombCoords[0], bombCoords[1]);
		int[] nonBombCoords = getFieldFromBoard(board, false);
		board.openField(nonBombCoords[0], nonBombCoords[1]);
		String result = board.getSerializedState();
		String[] lines = result.split("\\r?\\n");

		// Check that the first line has the time
		Assertions.assertEquals("00:00", lines[0]);

		// Check the dimensions of the output
		Assertions.assertEquals(height + 1, lines.length);
		for (int i = 1; i < lines.length; i++) {
			Assertions.assertEquals(width, lines[i].length());
		}

		/**
		 * Since we flagged 1 bomb, we expect there to be `bombs-1` occurences of the
		 * UNOPENED_WITH_BOMB status
		 */
		Assertions.assertEquals(bombs - 1, result.chars().filter(c -> c == Field.UNOPENED_WITH_BOMB).count(), result);

		/**
		 * We also expect to find 1 flagged field
		 */
		Assertions.assertEquals(1, result.chars().filter(c -> c == Field.FLAGGED_WITH_BOMB).count(), result);
	}

	@Test
	public void testGetDifficulty() {
		for (DifficultyLevel level : DifficultyLevel.getAll()) {
			Board board = new Board(level);
			Assertions.assertEquals(level, board.getDifficulty());
		}
	}

}

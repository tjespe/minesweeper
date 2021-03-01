package minesweeper.model;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {

	private Board board;
	private int bombs = 10;
	private int width = 10;
	private int height = 10;

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

	@BeforeEach
	public void setup() {
		board = new Board(height, width, bombs);
	}

	@Test
	public void testConstructor() {
		int countedBombs = 0;
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if (board.getFieldHasBomb(x, y))
					countedBombs++;
		Assertions.assertEquals(this.bombs, countedBombs);
	}

	@Test
	public void testGetAdjacentFields() {
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
		int x = -1, y = -1;
		for (int col = 0; col < 2; col++) {
			for (int row = 0; row < 2; row++) {
				if (!smallBoard.getFieldHasBomb(row, col)) {
					x = col;
					y = row;
				}
			}
		}

		// Check that we found the field
		Assertions.assertNotEquals(-1, x);
		Assertions.assertNotEquals(-1, y);

		// Check that the field has 3 adjacent bombs
		Assertions.assertEquals(3, smallBoard.countAdjacentBombs(x, y));
	}

	@Test
	public void testOpenField() {
		// TODO: Check that the method works
	}

	@Test
	public void testGetStatus() {
		// TODO: Check that the method works
	}

}

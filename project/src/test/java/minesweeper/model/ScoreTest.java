package minesweeper.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ScoreTest {

	@Test
	public void testCompareTo() {
		// given following scores
		Score score1 = new Score("Kristoffer", "01:30");
		Score score2 = new Score("Tord", "10:20");
		Assertions.assertTrue(score1.compareTo(score2) < 0, "score1 should be smaller than score2");

		// given following scores
		Score score3 = new Score("Kristoffer", "01:30");
		Score score4 = new Score("Tord", "01:30");
		Assertions.assertEquals(0, score3.compareTo(score4), "score3 should be equal to score4");

		// given following scores
		Score score5 = new Score("Kristoffer", "10:20");
		Score score6 = new Score("Tord", "01:30");
		Assertions.assertTrue(score5.compareTo(score6) > 0, "score5 should be bigger than score6");

	}

}

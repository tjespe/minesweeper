package minesweeper.fxui;

import java.util.ArrayList;
import java.util.Collection;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import minesweeper.model.Score;

public class HighscoreListController {

	@FXML
	private ListView<String> highscores;

	private MinesweeperController parentController;

	public void setParentController(MinesweeperController parentController) {
		this.parentController = parentController;
		this.initialize();
	}

	public void initialize() {
		loadHighscoreList();
	}

	private void loadHighscoreList() {
		if (parentController == null)
			return;
		System.out.println(parentController.highscores.getBestScores(parentController.getCurrentDifficultyLevel()));
		Collection<Score> topFive = new ArrayList<>(parentController.highscores.getBestScores(parentController.getCurrentDifficultyLevel()));
		for (Score score : topFive) {
			highscores.getItems().add(String.format("%-37s %10s", score.getName(), score.getTime()));
		}
	}
}
